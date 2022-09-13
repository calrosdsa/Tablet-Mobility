package com.coppernic.mobility.data.result

import androidx.room.Embedded
import androidx.room.Relation
import com.coppernic.mobility.data.models.entities.Cardholder
import com.coppernic.mobility.data.models.entities.ImageUser

data class DetailEntity(
    @Embedded val cardImage:ImageUser,
    @Relation(
        parentColumn = "userGui",
        entityColumn = "guid"
    )
    val cardHolder:Cardholder
)