package com.coppernic.mobility.data.result

import android.graphics.Bitmap

data class CardCredential(
    val firstName:String,
    val lastName:String,
    val facilityCode:Int?,
    val cardNumber:Int?,
    val guidCardHolder:String?,
    val picture:Bitmap?,
    val estado:String,
    val ci:String,
)
