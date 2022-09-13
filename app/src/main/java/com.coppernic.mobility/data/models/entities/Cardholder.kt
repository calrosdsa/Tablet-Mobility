package com.coppernic.mobility.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cardholder")
data class Cardholder(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val guid: String,
//    val firstName: String? = null,
//    val lastName: String? = null,
    val ci: String ?= null,
//    val picture: Bitmap? = null ,
    val descriptions: String? = null,
    val empresa: String? = null,
    val estado: String? = null,
):AppEntity