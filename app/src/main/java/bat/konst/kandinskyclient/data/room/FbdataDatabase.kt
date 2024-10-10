package bat.konst.kandinskyclient.data.room

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bat.konst.kandinskyclient.data.room.entity.Request

@Database(entities = [Request::class], version = 1)
abstract class FbdataDatabase: RoomDatabase() {

    abstract fun getFbdataDao(): FbdataDao

    companion object {
        @Volatile
        private var INSTANCE: FbdataDatabase? = null

        fun getInstance(context: Context): FbdataDatabase {
            return if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    FbdataDatabase::class.java,
                    "fbdata_database"
                )
                    .fallbackToDestructiveMigration()  // TODO: Remove migration after testing
                    .build()
                INSTANCE as FbdataDatabase
            } else {
                INSTANCE as FbdataDatabase
            }
        }

    }
}
