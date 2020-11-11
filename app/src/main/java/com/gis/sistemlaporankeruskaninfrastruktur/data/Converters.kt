package com.gis.sistemlaporankeruskaninfrastruktur.data

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by riza@deliv.co.id on 1/22/20.
 */

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time
}