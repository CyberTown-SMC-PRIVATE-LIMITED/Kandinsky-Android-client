package bat.konst.kandinskyclient.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import bat.konst.kandinskyclient.data.room.entity.Config
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.Request
import bat.konst.kandinskyclient.data.room.entity.RequestJoinImage

@Dao
interface FbdataDao {
    // request
    @Insert
    fun addRequest(request: Request)

    @Update
    fun updateRequest(request: Request)

    @Delete
    fun deleteRequest(request: Request)

    @Query("SELECT * FROM requests WHERE md5 = :md5")
    fun getRequest(md5: String): Request?

    // используется на главном экране
    @Query("SELECT " +
            "    r.md5, " +
            "    r.prompt, " +
            "    r.negative_prompt, " +
            "    r.style, " +
            "    i.status, " +
            "    i.image_thumbnail_base64 " +
            "FROM requests r " +
            "LEFT JOIN ( " +
            "    SELECT md5, status, image_thumbnail_base64 " +
            "    FROM images i " +
            "    WHERE id = ( " +
            "        SELECT MAX(id) " +
            "        FROM images " +
            "        WHERE images.md5 = i.md5 " +
            "    ) " +
            ") i ON i.md5 = r.md5 " +
            "WHERE r.deleted=0 " +
            "ORDER BY r.date_update DESC "

    )
    fun getAllRequestJoinImages(): List<RequestJoinImage>

    @Query("UPDATE requests SET deleted=1 WHERE md5 = :md5")
    fun markDeletedRequest(md5: String)

    @Query("SELECT * FROM requests WHERE deleted = 1")
    fun getAllMarketToDeleteRequests(): List<Request>



    // image
    @Insert
    fun addImage(image: Image)

    @Delete
    fun deleteImage(image: Image)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getImage(id: Long): Image?

    @Query("SELECT * FROM images WHERE status = :status ORDER BY id LIMIT 1")
    fun getFirstImageByStatus(status: Int): Image?

    @Update
    fun updateImage(image: Image)

    @Query("SELECT * FROM images WHERE md5 = :md5 ORDER BY id DESC")
    fun getImages(md5: String): List<Image>

    @Query("SELECT * FROM images WHERE status = :status")
    fun getImagesByStatus(status: Int): List<Image>

    @Query("SELECT * FROM images WHERE md5 = :md5 AND id > :id AND status = :status ORDER BY id LIMIT 1")
    fun getNextImage(md5: String, id: Long, status: Int): Image?

    @Query("SELECT * FROM images WHERE md5 = :md5 AND id < :id AND status = :status ORDER BY id DESC LIMIT 1")
    fun getPrevImage(md5: String, id: Long, status: Int): Image?

    // config
    @Query("SELECT * FROM config WHERE name = :name")
    fun getConfigByName(name: String): Config?

    @Insert (onConflict = androidx.room.OnConflictStrategy.REPLACE)
    fun setConfig(config: Config)

}