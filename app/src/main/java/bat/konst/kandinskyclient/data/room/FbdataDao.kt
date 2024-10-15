package bat.konst.kandinskyclient.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import bat.konst.kandinskyclient.data.room.entity.Config
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.Request

@Dao
interface FbdataDao {
    // request
    @Insert
    fun addRequest(request: Request)

    @Update
    fun updateRequest(request: Request)

    @Delete
    fun deleteRequest(request: Request)

    @Query("SELECT * FROM requests")
    fun getAllRequests(): List<Request>

    @Query("SELECT * FROM requests WHERE md5 = :md5 ORDER BY date_update DESC")
    fun getRequest(md5: String): Request?

    // image
    @Insert
    fun addImage(image: Image)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getImage(id: Long): Image?

    @Query("SELECT * FROM images WHERE md5 = :md5")
    fun getImages(md5: String): List<Image>

    // config
    @Query("SELECT * FROM config WHERE name = :name")
    fun getConfigByName(name: String): Config?

    @Insert (onConflict = androidx.room.OnConflictStrategy.REPLACE)
    fun setConfig(config: Config)

}