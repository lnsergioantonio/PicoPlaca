package com.sergio.testpicoplaca.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contravention::class],version=1)
abstract class ContraventionDatabase : RoomDatabase() {
    abstract fun contraventionDao(): ContraventionDao

    companion object {
        private val DATABASE_NAME = "contravaction_database"
        @Volatile
        private var INSTANCE : ContraventionDatabase? = null

        fun getAppDatabase(context: Context): ContraventionDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null)
                return tempInstance
            synchronized(this){
                val instance = Room.databaseBuilder(context,
                        ContraventionDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        fun destroyDataBase(){
            INSTANCE =null
        }
    }
}