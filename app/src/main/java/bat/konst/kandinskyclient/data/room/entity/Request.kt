package bat.konst.kandinskyclient.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class Request(

    @PrimaryKey
    @ColumnInfo(name = "md5")
    val md5: String,  // md5(prompt + negative_prompt + style)

    @ColumnInfo(name = "prompt")
    val prompt: String,

    @ColumnInfo(name = "negative_prompt")
    val negativePrompt: String,

    @ColumnInfo(name = "style")
    val style: String,

    @ColumnInfo(name = "date_create")
    val dateCreate: Long,

    @ColumnInfo(name = "date_update")
    val dateUpdate: Long,

)
