package bat.konst.kandinskyclient.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import bat.konst.kandinskyclient.data.room.entity.Config
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.Request

@Database(entities = [Request::class, Image::class, Config::class], version = 8)
abstract class FbdataDatabase: RoomDatabase() {

    abstract fun getFbdataDao(): FbdataDao

}
