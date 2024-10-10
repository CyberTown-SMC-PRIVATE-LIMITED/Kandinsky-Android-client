package bat.konst.kandinskyclient.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import bat.konst.kandinskyclient.data.room.entity.Request

@Dao
interface FbdataDao {
    @Insert
    fun addRequest(request: Request)

    @Update
    fun updateRequest(request: Request)

    @Delete
    fun deleteRequest(request: Request)

    @Query("SELECT * FROM requests")
    fun getAllRequests(): List<Request>

    @Query("SELECT * FROM requests WHERE md5 = :md5 ORDER BY date_update DESC")
    fun getRequest(md5: String): Request

}