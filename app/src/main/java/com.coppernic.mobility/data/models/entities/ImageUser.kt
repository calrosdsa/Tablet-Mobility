package com.coppernic.mobility.data.models.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "image_user"
)
data class ImageUser(
    @PrimaryKey val userGui:String,
    val picture:Bitmap? = null,
    val nombre:String? = " ",
)
