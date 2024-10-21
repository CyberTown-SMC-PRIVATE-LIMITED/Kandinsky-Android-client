package bat.konst.kandinskyclient.data.room.entity

import androidx.room.ColumnInfo

data class RequestJoinImage(
    // Модель для объединённого запроса Requests & Image -- для вывода общего списка запросов
    // request
    @ColumnInfo(name = "md5")
    val md5: String,  // md5(prompt + negative_prompt + style)

    @ColumnInfo(name = "prompt")
    val prompt: String,

    @ColumnInfo(name = "negative_prompt")
    val negativePrompt: String,

    @ColumnInfo(name = "style")
    val style: String,

    // image
    @ColumnInfo(name = "status")
    val status: Int,

    @ColumnInfo(name = "image_thumbnail_base64")
    val imageThumbnailBase64: String
)
