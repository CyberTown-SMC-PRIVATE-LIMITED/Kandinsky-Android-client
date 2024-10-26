package bat.konst.kandinskyclient.data.fileStorage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import bat.konst.kandinskyclient.app.FILE_IMAGE_THUMB_HEIGHT
import bat.konst.kandinskyclient.app.FILE_IMAGE_THUMB_WIDTH
import bat.konst.kandinskyclient.app.FILE_STORAGE_PATH
import java.io.ByteArrayOutputStream
import java.io.File
import org.apache.commons.codec.binary.Base64 as ApacheBase64


// repositoru for storage image files & thumbs
fun saveImageFile(base64: String, id: Long): String {
    // Сохраняет файл из base64 строки и возвращает путь к нему
    val imageFileName = getImageNameById(id)
    if (base64ToBinaryAndSaveToFile(base64, imageFileName)) {
        return imageFileName
    }
    return ""
}

fun saveImageThumbinal(id: Long): String {
    // из файла изображения с заданным id делает thumb и возвращает путь к нему
    val imageFileName = getImageNameById(id)
    val thumbFileName = getThumbinalNameById(id)
    if (makeThumbinal(imageFileName, thumbFileName, FILE_IMAGE_THUMB_HEIGHT)) {
        return thumbFileName
    }
    return ""
}

fun deleteImageAndThumbinal(id: Long) {
    fun deleteFile(filePath: String) {
        // удаляет файл по имени
        val file = File(filePath)
        if (file.exists()) {
            try {
                file.delete()
            } catch (e: Exception) {
                return
            }
        }
    }
    // удаляем изображения и thumb для указанного id изображения
    deleteFile(getImageNameById(id))
    deleteFile(getThumbinalNameById(id))
}


// ------
internal fun getImageNameById(id: Long): String {
    return File(FILE_STORAGE_PATH).resolve("$id.jpg").toString()
}

internal fun getThumbinalNameById(id: Long): String {
    return File(FILE_STORAGE_PATH).resolve("$id.thumb.png").toString()
}

internal fun base64ToBinaryAndSaveToFile(base64String: String, fileName: String): Boolean {
    // сохраняет файл из base64 строки
    // возвращает true в случае успеха
    try {
        val base64 = ApacheBase64()
        val decodedBytes = base64.decode(base64String)

        File(fileName).writeBytes(decodedBytes)
        return true
    } catch (e: Exception) {
        return false
    }
}

internal fun makeThumbinal(fileName: String, thumbitalName: String, thumbinalHeight: Int): Boolean {
    // возвращает true в случае успеха
    try {
        val bitmap = BitmapFactory.decodeFile(fileName)
        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap,
            FILE_IMAGE_THUMB_WIDTH,
            FILE_IMAGE_THUMB_HEIGHT,
            false
        )
        val bos = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val thumbBitmapdata = bos.toByteArray()
        File(thumbitalName).writeBytes(thumbBitmapdata)
    } catch (e: Exception) {
        return false
    }

    return true
}