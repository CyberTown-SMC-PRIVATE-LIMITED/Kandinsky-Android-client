package bat.konst.kandinskyclient.data.room

import bat.konst.kandinskyclient.app.CONFIG_DEFAULT_VALUE
import bat.konst.kandinskyclient.data.room.entity.Config
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.Request
import bat.konst.kandinskyclient.data.room.entity.RequestJoinImage
import bat.konst.kandinskyclient.data.room.entity.StatusTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class FbdataRepository @Inject constructor(private val fbdataDao: FbdataDao) {

    // ------------------ request
    private suspend fun getMd5Hash(prompt: String, negativePrompt: String, style: String): String {
        // https://stackoverflow.com/questions/64171624/how-to-generate-an-md5-hash-in-kotlin
        val stringKey = "$prompt+$negativePrompt+$style"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(stringKey.toByteArray())).toString(16).padStart(32, '0')
    }

    suspend fun addRequest(prompt: String, negativePrompt: String, style: String, qw: Int, onSuccess: () -> Unit = {}) {
        // добавляет запрос. Если он есть -- обновляет его дату.
        // добавляет Image в очередь на обработку к FussionBrain.

        withContext(Dispatchers.IO) {
            val currentDate: Long = System.currentTimeMillis()

            // 1. Calculate md5 hash by prompt+negativePrompt+style
            val md5 = getMd5Hash(prompt, negativePrompt, style)

            // 2. find existing request by md5
            val currentRequest = fbdataDao.getRequest(md5)
            if (currentRequest == null) {
                // 3. if not found - add new request
                val request = Request(md5 = md5, prompt = prompt, negativePrompt = negativePrompt, style = style, dateCreate = currentDate, dateUpdate = currentDate)
                fbdataDao.addRequest(request)
            } else {
                // 4. update request
                val request = Request(md5 = md5, prompt = prompt, negativePrompt = negativePrompt, style = style, dateCreate = currentRequest.dateCreate, dateUpdate = currentDate)
                fbdataDao.updateRequest(request)
            }

            // 5. add new image (to fussion brain queeue) QW times
            repeat(qw) {
                val image = Image(
                    md5 = md5,
                    kandinskyId = "",
                    status = StatusTypes.NEW.value,
                    dateCreated = currentDate,
                    imageBase64 = "",
                    imageThumbnailBase64 = ""
                )
                fbdataDao.addImage(image)
            }
        }
        onSuccess()
    }

    suspend fun getRequest(md5: String): Request {
        var request: Request?
        withContext(Dispatchers.IO) {
            request = fbdataDao.getRequest(md5)
        }
        if (request == null) {
            return Request(md5 = "", prompt = "", negativePrompt = "", style = "", dateCreate = 0, dateUpdate = 0)
        }
        return request!!
    }

    suspend fun getAllRequests(): List<Request> {
        val reqs: List<Request>
        withContext(Dispatchers.IO) {
             reqs = fbdataDao.getAllRequests()
        }
        return reqs
    }

    suspend fun getAllRequestJoinImages(): List<RequestJoinImage> {
        val reqs: List<RequestJoinImage>
        withContext(Dispatchers.IO) {
            reqs = fbdataDao.getAllRequestJoinImages()
        }
        return reqs
    }

    // ------------------ image
    suspend fun getImages(md5: String): List<Image> {
        val images: List<Image>
        withContext(Dispatchers.IO) {
            images = fbdataDao.getImages(md5)
        }
        return images
    }

    suspend fun getImagesByStatus(status: Int): List<Image> {
        val images: List<Image>
        withContext(Dispatchers.IO) {
            images = fbdataDao.getImagesByStatus(status)
        }
        return images
    }

    suspend fun getFirstImageByStatus(status: Int): Image? {
        var image: Image? = null
        withContext(Dispatchers.IO) {
            image = fbdataDao.getFirstImageByStatus(status)
        }
        return image
    }

    suspend fun getImage(id: Long): Image {
        var image: Image?
        withContext(Dispatchers.IO) {
            image = fbdataDao.getImage(id)
        }
        if (image == null) {
            return Image(id = 0, md5 = "", kandinskyId = "", status = 0, dateCreated = 0, imageBase64 = "", imageThumbnailBase64 = "")
        }
        return image!!
    }

    suspend fun updateImage(image: Image) {
        withContext(Dispatchers.IO) {
            fbdataDao.updateImage(image)
        }
    }

    suspend fun getNextImageId(md5: String, id: Long): Long? {
        var nextId: Long? = null
        withContext(Dispatchers.IO) {
            nextId = fbdataDao.getNextImage(md5, id, StatusTypes.DONE.value)?.id
        }
        return nextId
    }

    suspend fun getPrevImageId(md5: String, id: Long): Long? {
        var nextId: Long? = null
        withContext(Dispatchers.IO) {
            nextId = fbdataDao.getPrevImage(md5, id, StatusTypes.DONE.value)?.id
        }
        return nextId
    }

    // ------------------ config
    suspend fun getConfigByName(name: String): String {
        // получаем параметр конфигурации по заданному имени
        var config: Config?
        withContext(Dispatchers.IO) {
            config = fbdataDao.getConfigByName(name)
        }
        if (config != null) {
            return config!!.value
        }
        return CONFIG_DEFAULT_VALUE
    }

    suspend fun setConfig(name: String, value: String) {
        withContext(Dispatchers.IO) {
            val config = Config(name = name, value = value)
            fbdataDao.setConfig(config)
        }
    }
}