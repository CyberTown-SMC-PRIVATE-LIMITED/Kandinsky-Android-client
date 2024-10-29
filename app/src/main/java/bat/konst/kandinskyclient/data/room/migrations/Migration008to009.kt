package bat.konst.kandinskyclient.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_008_009 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE images ADD COLUMN remote_api_try_count INTEGER NOT NULL DEFAULT 0")
    }
}