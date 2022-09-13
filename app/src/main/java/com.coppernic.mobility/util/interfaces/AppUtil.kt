package com.coppernic.mobility.util.interfaces

import android.content.Context
import android.graphics.Bitmap
import java.time.OffsetDateTime

interface AppUtil {
    suspend fun getImageBitmap(context: Context, imageUrl:String?  ): Bitmap?
    suspend fun formatDateOffset():OffsetDateTime
}