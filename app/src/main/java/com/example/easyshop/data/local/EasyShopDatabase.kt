package com.example.easyshop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ClienteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class EasyShopDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao

    companion object {

        @Volatile
        private var INSTANCE: EasyShopDatabase? = null

        fun getInstance(context: Context): EasyShopDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EasyShopDatabase::class.java,
                    "easyshop.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}