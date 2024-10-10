package bat.konst.kandinskyclient.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import bat.konst.kandinskyclient.data.room.entity.Request

@Database(entities = [Request::class], version = 1)
abstract class FbdataDatabase: RoomDatabase() {

    abstract fun getFbdataDao(): FbdataDao

}
