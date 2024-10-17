package bat.konst.kandinskyclient.data.fileStorage

// repositoru for storage image files & thumbs
fun SaveImageFile(base64: String, id: Long): String {
    // Сохраняет файл из base64 строки и возвращает путь к нему
    val imageFileName = getImageNameById(id)
    return imageFileName
}

fun SaveImageThumbinal(id: Long): String {
    // из файла изображения с заданным id делает thumb и возвращает путь к нему
    val thumbFileName = getImageNameById(id)
    return thumbFileName
}

// ------
internal fun getImageNameById(id: Long): String {
    // val storagePath = getFilesDir()
    return ""
}

internal fun getThumbinalNameById(id: Long): String {
    // val storagePath = getFilesDir()
    return ""

}