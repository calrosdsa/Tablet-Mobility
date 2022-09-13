package com.coppernic.mobility.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ciudad")
data class Ciudad(
    @PrimaryKey val id: Int,
    val nombre: String,
)
