package com.example.mist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlataformEntity::class, CorEntity::class, MarcaEntity::class],
    version = 1
)
abstract class PlataformDatabaseProvider : RoomDatabase() {
    abstract fun PlataformDAO(): PlataformDAO
    abstract fun CorDAO(): CorDAO
    abstract fun MarcaDAO(): MarcaDAO

    companion object {
        @Volatile
        private var INSTANCE: PlataformDatabaseProvider? = null
        fun provide(context: Context): PlataformDatabaseProvider {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlataformDatabaseProvider::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}