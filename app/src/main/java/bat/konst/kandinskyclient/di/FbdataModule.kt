package bat.konst.kandinskyclient.di

import android.content.Context
import androidx.room.Room
import bat.konst.kandinskyclient.data.room.FbdataDao
import bat.konst.kandinskyclient.data.room.FbdataDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

// di (dependency injection) for room database
@Module
object FbdataModule {
    @Provides
    @Singleton
    fun provideFbdataDatabase(@ApplicationContext context: Context): FbdataDatabase {
        return Room.databaseBuilder(
                    context,
                    FbdataDatabase::class.java,
                    "fbdata_database"
                ).fallbackToDestructiveMigration()  // TODO: Remove migration after testing
                 .build()
    }

    @Provides
    @Singleton
    fun provideFbdataDao(fbdataDatabase: FbdataDatabase): FbdataDao {
        return fbdataDatabase.getFbdataDao()
    }
}
