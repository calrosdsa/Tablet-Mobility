package com.coppernic.mobility.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.coppernic.mobility.data.dto.access.AccessValue
import com.coppernic.mobility.data.dto.settings.Ciudade
import com.coppernic.mobility.data.dto.settings.Coordenada
import com.google.gson.Gson
import org.threeten.bp.Instant
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.io.ByteArrayOutputStream

object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

//    @TypeConverter
//    fun fromTimestamp(value: Long?): Date? {
//        return value?.let { Date(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: Date?): Long? {
//        return if (date == null) null else date.getTime()
//    }

    @TypeConverter
    @JvmStatic
    fun accessToJsonString(value:List<AccessValue>):String = Gson().toJson((value))

    @TypeConverter
    @JvmStatic
    fun jsonToAccess(value:String) = Gson().fromJson(value,Array<AccessValue>::class.java).toList()
    @TypeConverter
    @JvmStatic
    fun citiestoJsonString(value:List<Ciudade>):String = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun jsonCitiesToList(value:String) = Gson().fromJson(value,Array<Ciudade>::class.java).toList()

    @TypeConverter
    @JvmStatic
    fun listToJsonString(value: List<Coordenada>): String = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<Coordenada>::class.java).toList()


    @TypeConverter
    @JvmStatic
    fun toZoneId(value: String?) = value?.let { ZoneId.of(it) }

    @TypeConverter
    @JvmStatic
    fun fromZoneId(value: ZoneId?) = value?.id

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?) = value?.let { LocalTime.parse(value) }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime?) = value?.format(DateTimeFormatter.ISO_LOCAL_TIME)

    @TypeConverter
    @JvmStatic
    fun fromBitmap(bitmap: Bitmap?): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    @JvmStatic
    fun toBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    @JvmStatic
    fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    @JvmStatic
    fun fromInstant(date: Instant?) = date?.toEpochMilli()
}