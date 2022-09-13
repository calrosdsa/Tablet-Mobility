package com.coppernic.mobility.domain.util

import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.coppernic.mobility.inject.MediumDate
import com.coppernic.mobility.inject.MediumDateTime
import com.coppernic.mobility.inject.ShortDate
import com.coppernic.mobility.inject.ShortTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AppDateFormatter @Inject constructor(
    @ShortTime private val shortTimeFormatter: DateTimeFormatter,
    @ShortDate private val shortDateFormatter: DateTimeFormatter,
    @MediumDate private val mediumDateFormatter: DateTimeFormatter,
    @MediumDateTime private val mediumDateTimeFormatter: DateTimeFormatter
) {

//    fun formatEpochLong(epochValue:Long?):String{
//        val localDate  = OffsetDateTime.of(epochValue?.let { LocalDateTime.ofEpochSecond(it,0 , ZoneOffset.UTC) }, ZoneOffset.UTC)
//        return shortDateFormatter.format(localDate)
//    }
//
//    fun formatShortDate(value:Long?): String {
//        val localDate  = OffsetDateTime.of(value?.let { LocalDateTime.ofEpochSecond(it,0 , ZoneOffset.UTC) }, ZoneOffset.UTC)
//        return shortDateFormatter.format(localDate)
//    }
//
//    fun formatMediumDate(value: Long?): String {
//        val localDate  = OffsetDateTime.of(value?.let { LocalDateTime.ofEpochSecond(it,0 , ZoneOffset.UTC) }, ZoneOffset.UTC)
//        return mediumDateFormatter.format(localDate)
//    }
//
//    fun formatMediumDateTime(value: Long?): String {
//        val localDate  = OffsetDateTime.of(value?.let { LocalDateTime.ofEpochSecond(it,0 , ZoneOffset.UTC) }, ZoneOffset.UTC)
//        return mediumDateTimeFormatter.format(localDate)
//    }

//    fun formatShortTime(localTime: LocalTime): String = shortTimeFormatter.format(localTime)

    fun formatShortDate(temporalAmount: Temporal): String = shortDateFormatter.format(temporalAmount)

    fun formatMediumDate(temporalAmount: Temporal): String = mediumDateFormatter.format(temporalAmount)

    fun formatMediumDateTime(temporalAmount: Temporal): String = mediumDateTimeFormatter.format(temporalAmount)

    fun formatShortTime(localTime: LocalTime): String = shortTimeFormatter.format(localTime)

//    fun formatShortRelativeTime(dateTime: OffsetDateTime): String {
//        val now = OffsetDateTime.now()
//
//        return if (dateTime.isBefore(now)) {
//            if (dateTime.year == now.year || dateTime.isAfter(now.minusDays(7))) {
//                Log.d("DATE1", "OPCION 1")
//                // Within the past week
//                DateUtils.getRelativeTimeSpanString(
//                    dateTime.toInstant().toEpochMilli(),
//                    System.currentTimeMillis(),
//                    DateUtils.MINUTE_IN_MILLIS,
//                    DateUtils.FORMAT_SHOW_DATE
//                ).toString()
//            } else {
//                // More than 7 days ago
//                Log.d("DATE1", "OPCION 2")
//                formatShortDate(dateTime)
//
//            }
//        } else {
//            if (dateTime.year == now.year || dateTime.isBefore(now.plusDays(14))) {
//                Log.d("DATE1", "OPCION 3")
//                // In the near future (next 2 weeks)
//                DateUtils.getRelativeTimeSpanString(
//                    dateTime.toInstant().toEpochMilli(),
//                    System.currentTimeMillis(),
//                    DateUtils.MINUTE_IN_MILLIS,
//                    DateUtils.FORMAT_SHOW_DATE
//                ).toString()
//            } else {
//                // In the far future
//                Log.d("DATE1", "OPCION 4")
//                formatShortDate(dateTime)
//            }
//        }
//    }


    fun getDateTime(date:String): CharSequence? {
        Log.d("DATESUB" , date.subSequence(5,7).toString())
        Log.d("DATESUB" , date.subSequence(8,10).toString())

        val y = date.take(4)
        val m = date.subSequence(5,7).toString()
        val d = date.subSequence(8,10).toString()
        val timeDate = date.drop(10)
        val newDate = String.format("%s-%s-%s $timeDate",m,d,y)
        Log.d("DATESUB" , newDate)
        //val newDate = date.replace("/","-")
        //val newDate = "07-06-2022 19:02:37"
        val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time:Long = sdf.parse(newDate)?.time ?: 0L;
        val now:Long = System.currentTimeMillis();
        return  DateUtils.getRelativeTimeSpanString(
            time,
            now,
            //    DateUtils.MINUTE_IN_MILLIS);
            DateUtils.MINUTE_IN_MILLIS);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(date:String): CharSequence? {

        val newDate = date.replace("/","-")
        //val newDate = "07-06-2022 19:02:37"
        val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time:Long = sdf.parse(newDate)?.time ?: 0L;
        val now:Long = System.currentTimeMillis();
        return  DateUtils.getRelativeTimeSpanString(
            time,
            now,
            //    DateUtils.MINUTE_IN_MILLIS);
            DateUtils.MINUTE_IN_MILLIS);
    }
}