package com.zahid.coolshoptest.local_db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.*
import androidx.room.RoomDatabase
import com.zahid.coolshoptest.local_db.dao.UserModelDao
import com.zahid.coolshoptest.local_db.entity.UserModelRoom

@Database(entities = [UserModelRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userModelDao(): UserModelDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    ).also {
                        INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "coolShopTest.db"
            ).build()
    }
}