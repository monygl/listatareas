package com.example.listatareas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.listatareas.helper.DateConverter

@Database(entities =arrayOf(TaskEntry::class),version=1,exportSchema=false )
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    companion object{
        private var Instance:AppDatabase?=null
        fun  getInstance(context: Context):AppDatabase?{
            if (Instance==null){
                synchronized(AppDatabase::class){
                    Instance= Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "todoList.db"
                    ).build()
                }
            }
            return Instance
        }
    }
    abstract fun taskDao():TaskDao

}