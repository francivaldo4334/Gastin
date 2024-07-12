package br.com.fcr.gastin.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DB {
    fun getService(context: Context): MyDatabase {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN createdAt INTEGER")
                db.execSQL("ALTER TABLE users ADD COLUMN isActive INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE users ADD COLUMN isVerified INTEGER NOT NULL DEFAULT 0")
            }

        }
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            MyDatabase.NAME
        ).addMigrations(

        ).fallbackToDestructiveMigration().build()
    }
}