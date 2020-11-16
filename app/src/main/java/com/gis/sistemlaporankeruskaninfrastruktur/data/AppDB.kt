package com.gis.sistemlaporankeruskaninfrastruktur.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Created by riza@deliv.co.id on 8/26/20.
 */


@Database(
    entities = [
        PostModel::class, CategoryModel::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {

    abstract fun posDao(): PostDao

    companion object {
//        @Volatile
//        private var INSTANCE: AppDB? = null

        fun getDatabase(
            context: Context
        ): AppDB {

            return /*INSTANCE ?: synchronized(this) {
                val instance =*/ Room.databaseBuilder(
                context.applicationContext,
                AppDB::class.java,
                "sipki-db"
            )
                .fallbackToDestructiveMigration()
                .build()
            /* INSTANCE = instance

             instance
         }*/
        }
    }
}
