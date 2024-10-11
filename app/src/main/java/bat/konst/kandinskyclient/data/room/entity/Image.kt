package bat.konst.kandinskyclient.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
    indices = [Index(value = ["md5"])]
)
data class Image (

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "md5")
    val md5: String,

    @ColumnInfo(name = "kandinsky_id")
    val kandinskyId: String,

    @ColumnInfo(name = "status")
    val status: Int,

    @ColumnInfo(name = "date_created")
    val dateCreated: Long,

    @ColumnInfo(name = "image_base64")
    val imageBase64: String,

)