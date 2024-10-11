package bat.konst.kandinskyclient.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class StatusTypes(val value: Int) {
    NEW(0),
    PROCESSING(1),
    DONE(2),
    ERROR(3);
    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}

@Entity(
    tableName = "images",
    indices = [
        Index(value = ["md5"]),
        Index(value = ["kandinsky_id"]),
        Index(value = ["status", "date_created"]),
    ]
)
data class Image (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

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