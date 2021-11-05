package com.dementiaquiz.android.database.typeConverter

import androidx.room.TypeConverter
import java.util.*

/**
 * Type converter for converting dates to/from long
 */
class Converter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}