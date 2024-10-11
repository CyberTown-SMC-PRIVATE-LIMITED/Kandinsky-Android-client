package bat.konst.kandinskyclient.data.room

import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.Request
import bat.konst.kandinskyclient.data.room.entity.StatusTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class FbdataRepository @Inject constructor(private val fbdataDao: FbdataDao) {

    private suspend fun getMd5Hash(prompt: String, negativePrompt: String, style: String): String {
        // https://stackoverflow.com/questions/64171624/how-to-generate-an-md5-hash-in-kotlin
        val stringKey = "$prompt+$negativePrompt+$style"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(stringKey.toByteArray())).toString(16).padStart(32, '0')
    }

    suspend fun addRequest(prompt: String, negativePrompt: String, style: String, onSuccess: () -> Unit = {}) {
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

            // 5. add new image (to fussion brain queeue)
            val image = Image(md5 = md5, kandinskyId = "", status = StatusTypes.NEW.value, dateCreated = currentDate, imageBase64 = "")
            fbdataDao.addImage(image)
        }
        onSuccess()
    }
}