package ru.diplomnaya.skilllcinema.model.database

import android.content.Context
import androidx.room.Room

object DataBase {
    var INSTANCE: CollectionsFilmsDatabase? = null
        private set

    fun initDatabase(context: Context) {
        if (INSTANCE == null) {
            synchronized(CollectionsFilmsDatabase::class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        CollectionsFilmsDatabase::class.java,
                        CollectionsFilmsDatabase.DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                }
            }
        }
    }
}
