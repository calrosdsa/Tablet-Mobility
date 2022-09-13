package com.coppernic.mobility.domain.util

import org.threeten.bp.OffsetDateTime
import kotlin.math.absoluteValue

class HandlerTime {
    fun getTodayRangeDate(currentDate:OffsetDateTime,){
        currentDate.hour
    }
}

fun OffsetDateTime.getInitialDay():OffsetDateTime{
    return when(this.hour.absoluteValue){
        0 -> this.minusHours(1)
        1 -> this.minusHours(2)
        2 -> this.minusHours(3)
        3 -> this.minusHours(4)
        4 -> this.minusHours(5)
        5 -> this.minusHours(6)
        6 -> this.minusHours(7)
        7 -> this.minusHours(8)
        8 -> this.minusHours(9)
        9 -> this.minusHours(10)
        10 -> this.minusHours(11)
        11 -> this.minusHours(12)
        12 -> this.minusHours(13)
        13 -> this.minusHours(14)
        14 -> this.minusHours(15)
        15 -> this.minusHours(16)
        16 -> this.minusHours(17)
        17 -> this.minusHours(18)
        18 -> this.minusHours(19)
        19 -> this.minusHours(20)
        20 -> this.minusHours(21)
        21 -> this.minusHours(22)
        22 -> this.minusHours(23)
        23 -> this.minusHours(24)
        else -> this
    }

}