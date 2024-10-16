package bat.konst.kandinskyclient.model.imageWorker

import bat.konst.kandinskyclient.app.CONFIG_DEFAULT_VALUE
import bat.konst.kandinskyclient.app.CONFIG_XKEY
import bat.konst.kandinskyclient.app.CONFIG_XSECRET
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_DONE
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_FAIL
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_INITIAL
import bat.konst.kandinskyclient.app.KANDINSKY_MODEL_ID
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.StatusTypes
import javax.inject.Inject
import javax.inject.Singleton

class GenerateImages {

    suspend fun FusionBrainGo(fbdataRepository: FbdataRepository, kandinskyApiRepository: KandinskyApiRepository) {
        // 1. проверяем готовность изображений
        val isReadyToNewRequest = checkGeneratedImages(fbdataRepository, kandinskyApiRepository)

        // 2. Если очередь пуста, отправляем запрос на новую генерацию
        if (isReadyToNewRequest) {
            sendImageToGenerate(fbdataRepository, kandinskyApiRepository)
        }
    }

    // -------- проверки и изменения статусов
    private suspend fun checkGeneratedImages(fbdataRepository: FbdataRepository, kandinskyApiRepository: KandinskyApiRepository): Boolean {
        /*
            Получает список изображений-заданий из БД, отправляет их на генерацию и сохраняет результирующие изображения
            Возвращает true, если все изображения уже готовы к использованию (очередь ожидания пуста)
        */
        // 0. Key и Secret
        val key = "Key " + fbdataRepository.getConfigByName(CONFIG_XKEY)
        val secret = "Secret " + fbdataRepository.getConfigByName(CONFIG_XSECRET)

        // 1. Получаем список уже отправленных на генерацию изображений
        val imagesForGeneration = fbdataRepository.getImagesByStatus(StatusTypes.PROCESSING.value)

        // 2. Проверяем каждое в REST-API Fussion Brain -- есть ли уже готовое изображение
        for (image in imagesForGeneration) {

            val imageResult = kandinskyApiRepository.getRequesrtStatusOrImage(key, secret, image.kandinskyId)
                ?: // изображение сгенерировать не удалось - пропускаем (сетевая ошибка)
                continue

            if (imageResult.status == KANDINSKY_GENERATE_RESULT_DONE) {
                // изображение уже готово - сохраняем
                fbdataRepository.updateImage(
                    Image(
                        id = image.id,
                        md5 = image.md5,
                        kandinskyId = image.kandinskyId,
                        status = StatusTypes.DONE.value,
                        dateCreated = image.dateCreated,
                        imageBase64 = imageResult.images[0],
                        imageThumbnailBase64 = imageResult.images[0] // TODO: add Thumb creation
                    )
                )
                continue
            }

            if (imageResult.status == KANDINSKY_GENERATE_RESULT_FAIL) {
                // изображение сгенерировать не удалось - сохраняем (запрос не прошёл цензуру или 404 или ошибка авторизации)
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
                continue
            }
        }

        // 3. Проверяем -- осталось ли что-то для генерации
        return fbdataRepository.getImagesByStatus(StatusTypes.PROCESSING.value).isEmpty()
    }

    private suspend fun sendImageToGenerate(fbdataRepository: FbdataRepository, kandinskyApiRepository: KandinskyApiRepository) {
        /*
            Отправляет первое изображение из очереди на генерацию
        */
        // 0. Key и Secret
        val key = "Key " + fbdataRepository.getConfigByName(CONFIG_XKEY)
        val secret = "Secret " + fbdataRepository.getConfigByName(CONFIG_XSECRET)

        // 1. Получаем первое изображение из очереди - если таких нет - выход
        val image = fbdataRepository.getFirstImageByStatus(StatusTypes.NEW.value) ?: return
        val request = fbdataRepository.getRequest(image.md5)
        if (request.md5 == "") return

        // 2. Отправляем на генерацию
        val imageResult =
            kandinskyApiRepository.sendGgenerateImageRequest(key, secret, request.prompt, request.negativePrompt, request.style, KANDINSKY_MODEL_ID)

        // 3. Обновляем статус
        if (imageResult != null && imageResult.status == KANDINSKY_GENERATE_RESULT_INITIAL) {
            fbdataRepository.updateImage(
                Image(
                    id = image.id,
                    md5 = image.md5,
                    kandinskyId = image.kandinskyId,
                    status = StatusTypes.PROCESSING.value,
                    dateCreated = image.dateCreated,
                    imageBase64 = "",
                    imageThumbnailBase64 = ""
                )
            )
        }
    }
}