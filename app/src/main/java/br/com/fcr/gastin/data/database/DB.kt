package br.com.fcr.gastin.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DB {
    fun getService(context: Context): MyDatabase {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE TB_CATEGORIA ADD COLUMN START_DATE INTEGER NULL DEFAULT NULL")
                db.execSQL("ALTER TABLE TB_CATEGORIA ADD COLUMN END_DATE INTEGER NULL DEFAULT NULL")
                db.execSQL("ALTER TABLE TB_CATEGORIA ADD COLUMN IS_RECURRENT INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE TB_CATEGORIA ADD COLUMN IS_EVER_DAYS INTEGER NOT NULL DEFAULT 0")
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