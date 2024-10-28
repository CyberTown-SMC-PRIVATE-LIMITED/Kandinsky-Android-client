package bat.konst.kandinskyclient.logic.worker

import android.content.Context
import bat.konst.kandinskyclient.app.AppState
import bat.konst.kandinskyclient.app.CONFIG_XKEY
import bat.konst.kandinskyclient.app.CONFIG_XSECRET
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_DONE
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_FAIL
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_INITIAL
import bat.konst.kandinskyclient.app.KANDINSKY_MODEL_ID_UNDEFINED
import bat.konst.kandinskyclient.app.KANDINSKY_QUEUE_MAX
import bat.konst.kandinskyclient.app.KANDINSKY_REQUEST_UNTERVAL_SEC
import bat.konst.kandinskyclient.data.fileStorage.deleteImageAndThumbinal
import bat.konst.kandinskyclient.data.fileStorage.saveImageFile
import bat.konst.kandinskyclient.data.fileStorage.saveImageThumbinal
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.StatusTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class ImagesGenerator {

    suspend fun fusionBrainGo(fbdataRepository: FbdataRepository, kandinskyApiRepository: KandinskyApiRepository, context: Context) {
        // сразу удалим из базы если есть что-то помеченное для удаления
        if (deleteMarkerRequests(fbdataRepository)) {
            sendSygnalOnDataChange(context) // что-то удалили -- обновим экран
        }
        // если нечего отправлять на генерацию - выходим
        if (!fbdataRepository.hasQueuedImages()) {
            return
        }

        // 0 параметры для запросов к API Fusion Brain
        val key = "Key " + fbdataRepository.getConfigByName(CONFIG_XKEY)
        val secret = "Secret " + fbdataRepository.getConfigByName(CONFIG_XSECRET)
        // 0.1. Проверка наличия / или возможности получения номера версии - этим же проверяем -- лежит ли сервис
        val fusionBrainModelVersionId: String = kandinskyApiRepository.getModelVersionId(key, secret)
        if (fusionBrainModelVersionId == KANDINSKY_MODEL_ID_UNDEFINED) {
            // версия не найдена или сервис лежит
            return
        }

        while (fbdataRepository.hasQueuedImages()) {
            // 1. Подождем интервал между запросами
            withContext(Dispatchers.IO) {
                sleep(KANDINSKY_REQUEST_UNTERVAL_SEC * 1000)
            }

            // 2. удаление помеченных requests
            var isDataChanged = deleteMarkerRequests(fbdataRepository)

            // 3. проверяем готовность изображений и получаем их
            isDataChanged = isDataChanged || recieveGeneratedImages(fbdataRepository, kandinskyApiRepository, key, secret)

            // 4. Если очередь пуста, отправляем запрос на новую генерацию
            if (isImagesQueueFree(fbdataRepository)) {
                isDataChanged = isDataChanged ||  sendImageToGenerate(fbdataRepository, kandinskyApiRepository, key, secret, fusionBrainModelVersionId)
            }

            // 5. Если были изменения данных - посылаем уведомление
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
    private suspend fun isImagesQueueFree(fbdataRepository: FbdataRepository): Boolean {
        //  Проверяем -- можно ли добавлять задания на генерацию
        return fbdataRepository.getImagesByStatus(StatusTypes.PROCESSING.value).size < KANDINSKY_QUEUE_MAX
    }

    // -------- работа с очередью запросов
    private suspend fun recieveGeneratedImages(
        fbdataRepository: FbdataRepository,
        kandinskyApiRepository: KandinskyApiRepository,
        key: String,
        secret: String
    ): Boolean {
        /*
            Получает список изображений-заданий из БД, отправляет их на генерацию и сохраняет результирующие изображения
            Возвращает true, если были изменения данных
        */
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
                val imageFile = saveImageFile(imageResult.images[0], image.id)
                val thumbFile = saveImageThumbinal(image.id)
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

    private suspend fun sendImageToGenerate(
        fbdataRepository: FbdataRepository,
        kandinskyApiRepository: KandinskyApiRepository,
        key: String,
        secret: String,
        fusionBrainModelVersionId: String
    ): Boolean {
        /*
            Отправляет первое изображение из очереди на генерацию
            Возвращает true, если были изменения данных
        */
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
            kandinskyApiRepository.sendGenerateImageRequest(key, secret, request.prompt, request.negativePrompt, request.style, fusionBrainModelVersionId)

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


    private suspend fun deleteMarkerRequests(fbdataRepository: FbdataRepository): Boolean {
        var isDataChanged = false
        // удаляем помеченные запросы
        // 1. список запросов помеченных к удалению
        val requestsToDelete = fbdataRepository.getAllMarketToDeleteRequests()
        for (request in requestsToDelete) {
            val imagesToDelete = fbdataRepository.getImages(request.md5)
            for (image in imagesToDelete) {
                deleteImageAndThumbinal(image.id)
                fbdataRepository.deleteImage(image)
                isDataChanged = true
            }
            fbdataRepository.deleteRequest(request)
        }
        return isDataChanged
    }
}