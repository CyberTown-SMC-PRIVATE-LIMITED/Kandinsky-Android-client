package bat.konst.kandinskyclient.worker

import android.content.Context
import bat.konst.kandinskyclient.app.AppState
import bat.konst.kandinskyclient.app.CONFIG_XKEY
import bat.konst.kandinskyclient.app.CONFIG_XSECRET
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_DONE
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_FAIL
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_INITIAL
import bat.konst.kandinskyclient.app.KANDINSKY_MODEL_ID
import bat.konst.kandinskyclient.app.KANDINSKY_QUEUE_MAX
import bat.konst.kandinskyclient.app.KANDINSKY_REQUEST_UNTERVAL_SEC
import bat.konst.kandinskyclient.data.fileStorage.SaveImageFile
import bat.konst.kandinskyclient.data.fileStorage.SaveImageThumbinal
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.StatusTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class ImagesGenerator {

    suspend fun fusionBrainGo(fbdataRepository: FbdataRepository, kandinskyApiRepository: KandinskyApiRepository, context: Context) {

        // TODO: проверка -- ключи неверны, сервис лежит - выход из воркера
        while (hasProcessingImages(fbdataRepository) || hasNewImages(fbdataRepository)) {

            // 0. Подождем интервал между запросами
            withContext(Dispatchers.IO) {
                sleep(KANDINSKY_REQUEST_UNTERVAL_SEC * 1000)
            }

            // 1. проверяем готовность изображений и получаем их
            var isDataChanged = recieveGeneratedImages(fbdataRepository, kandinskyApiRepository)

            // 2. Если очередь пуста, отправляем запрос на новую генерацию
            if (isImagesQueueFree(fbdataRepository)) {
                isDataChanged = isDataChanged ||  sendImageToGenerate(fbdataRepository, kandinskyApiRepository)
            }

            // 3. Если были изменения данных - посылаем уведомление
            if (isDataChanged) {
                sendSygnalOnDataChange(context)
            }
        }
    }


    // -------- Отправка оповещения об измении данных в БД
    private fun sendSygnalOnDataChange(context: Context) {
        AppState.RoomDataChaged.value += 1
    }


    // -------- проверки и изменения статусов
    private suspend fun hasProcessingImages(fbdataRepository: FbdataRepository): Boolean {
        //  Проверяем -- осталось ли что-то на генерации
        return fbdataRepository.getImagesByStatus(StatusTypes.PROCESSING.value).isNotEmpty()
    }

    private suspend fun isImagesQueueFree(fbdataRepository: FbdataRepository): Boolean {
        //  Проверяем -- можно ли добавлять задания на генерацию
        return fbdataRepository.getImagesByStatus(StatusTypes.PROCESSING.value).size < KANDINSKY_QUEUE_MAX
    }

    private suspend fun hasNewImages(fbdataRepository: FbdataRepository): Boolean {
        //  Проверяем -- ожидает ли что-то генерации
        return fbdataRepository.getImagesByStatus(StatusTypes.NEW.value).isNotEmpty()
    }

    private suspend fun recieveGeneratedImages(fbdataRepository: FbdataRepository, kandinskyApiRepository: KandinskyApiRepository): Boolean {
        /*
            Получает список изображений-заданий из БД, отправляет их на генерацию и сохраняет результирующие изображения
            Возвращает true, если были изменения данных
        */
        // 0. Key и Secret
        val key = "Key " + fbdataRepository.getConfigByName(CONFIG_XKEY)
        val secret = "Secret " + fbdataRepository.getConfigByName(CONFIG_XSECRET)
        var isDataChanged = false

        // 1. Получаем список уже отправленных на генерацию изображений
        val imagesForGeneration = fbdataRepository.getImagesByStatus(StatusTypes.PROCESSING.value)

        // 2. Проверяем каждое в REST-API Fussion Brain -- есть ли уже готовое изображение
        for (image in imagesForGeneration) {

            val imageResult = kandinskyApiRepository.getRequesrtStatusOrImage(key, secret, image.kandinskyId)
                ?: // изображение сгенерировать не удалось - пропускаем (сетевая ошибка)
                continue

            // запрос не прошёл цензуру
            if (imageResult.censored) {
                fbdataRepository.updateImage(
                    Image(
                        id = image.id,
                        md5 = image.md5,
                        kandinskyId = image.kandinskyId,
                        status = StatusTypes.CENCORED.value,
                        dateCreated = image.dateCreated,
                        imageBase64 = "",
                        imageThumbnailBase64 = ""
                    )
                )
                isDataChanged = true
                continue
            }

            // изображение уже готово - сохраняем
            if (imageResult.status == KANDINSKY_GENERATE_RESULT_DONE) {
                val imageFile = SaveImageFile(imageResult.images[0], image.id)
                val thumbFile = SaveImageThumbinal(image.id)
                fbdataRepository.updateImage(
                    Image(
                        id = image.id,
                        md5 = image.md5,
                        kandinskyId = image.kandinskyId,
                        status = StatusTypes.DONE.value,
                        dateCreated = image.dateCreated,
                        imageBase64 = imageFile,
                        imageThumbnailBase64 = thumbFile
                    )
                )
                isDataChanged = true
                continue
            }

            if (imageResult.status == KANDINSKY_GENERATE_RESULT_FAIL) {
                // изображение сгенерировать не удалось - сохраняем (ошибка 404 или ошибка авторизации)
                fbdataRepository.updateImage(
                    Image(
                        id = image.id,
                        md5 = image.md5,
                        kandinskyId = image.kandinskyId,
                        status = StatusTypes.ERROR.value,
                        dateCreated = image.dateCreated,
                        imageBase64 = "",
                        imageThumbnailBase64 = ""
                    )
                )
                isDataChanged = true
                continue
            }
        }

        return isDataChanged

    }

    private suspend fun sendImageToGenerate(fbdataRepository: FbdataRepository, kandinskyApiRepository: KandinskyApiRepository): Boolean {
        /*
            Отправляет первое изображение из очереди на генерацию
            Возвращает true, если были изменения данных
        */

        // 0. Key и Secret
        val key = "Key " + fbdataRepository.getConfigByName(CONFIG_XKEY)
        val secret = "Secret " + fbdataRepository.getConfigByName(CONFIG_XSECRET)


        // 1. Получаем первое изображение из очереди - если таких нет - выход
        val image = fbdataRepository.getFirstImageByStatus(StatusTypes.NEW.value) ?: return false
        // 1.a если для задания нет запроса - изменяем статус на ошибку - и выход
        val request = fbdataRepository.getRequest(image.md5)
        if (request.md5 == "") {
            fbdataRepository.updateImage(
                Image(
                    id = image.id,
                    md5 = image.md5,
                    kandinskyId = image.kandinskyId,
                    status = StatusTypes.ERROR.value,
                    dateCreated = image.dateCreated,
                    imageBase64 = "",
                    imageThumbnailBase64 = ""
                )
            )
            return true
        }

        // 2. Отправляем на генерацию
        val imageResult =
            kandinskyApiRepository.sendGgenerateImageRequest(key, secret, request.prompt, request.negativePrompt, request.style, KANDINSKY_MODEL_ID)

        // 3. Обновляем статус
        if (imageResult != null && imageResult.status == KANDINSKY_GENERATE_RESULT_INITIAL) {
            fbdataRepository.updateImage(
                Image(
                    id = image.id,
                    md5 = image.md5,
                    kandinskyId = imageResult.uuid,
                    status = StatusTypes.PROCESSING.value,
                    dateCreated = image.dateCreated,
                    imageBase64 = "",
                    imageThumbnailBase64 = ""
                )
            )
            return true
        }
        // TODO: проверка статусов с ошибками (неверный ключ, данные некорректны, сервис лежит)

        return false
    }
}