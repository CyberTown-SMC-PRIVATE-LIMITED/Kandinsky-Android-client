package bat.konst.kandinskyclient.di

import android.content.Context
import androidx.room.Room
import bat.konst.kandinskyclient.data.room.FbdataDao
import bat.konst.kandinskyclient.data.room.FbdataDatabase
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.data.room.migrations.MIGRATION_008_009
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// di (dependency injection) for room database
@Module
@InstallIn(SingletonComponent::class)
object FbdataModule {
    @Provides
    @Singleton
    fun provideFbdataDatabase(@ApplicationContext context: Context): FbdataDatabase {
        return Room.databaseBuilder(
                    context,
                    FbdataDatabase::class.java,
                    "fbdata_database"
                )
                 // .fallbackToDestructiveMigration()  // destructive migrations disabled
                 .addMigrations(MIGRATION_008_009)
                 .build()
    }

    @Provides
    @Singleton
    fun provideFbdataDao(fbdataDatabase: FbdataDatabase): FbdataDao {
        return fbdataDatabase.getFbdataDao()
    }

    @Provides
    @Singleton
    fun provideFbdataRepository(fbdataDao: FbdataDao): FbdataRepository {
        return FbdataRepository(fbdataDao)
    }
}
