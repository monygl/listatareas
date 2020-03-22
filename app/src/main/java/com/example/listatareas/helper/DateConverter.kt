package com.example.listatareas.helper

import androidx.room.TypeConverter
import java.util.*


class DateConverter {
    @TypeConverter
    fun toDate(timeStamp:Long?): Date?{
        return if(timeStamp !=null)Date(timeStamp) else null
    }
    @TypeConverter
    fun toTimeStamp(date:Date):Long? = date?.time
}