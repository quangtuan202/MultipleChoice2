package com.example.multiplechoice2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Question::class], version = 1, exportSchema = false)
abstract class QuestionDatabase: RoomDatabase() {
    companion object{
        private var INSTANCE: QuestionDatabase?=null
        fun getInstance(context: Context): QuestionDatabase {
            synchronized(this){
                var instance= INSTANCE;
                if(instance==null){
                    instance= Room.databaseBuilder(context.applicationContext, QuestionDatabase::class.java, "question_database").build()
                    INSTANCE =instance
                }
                return instance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}