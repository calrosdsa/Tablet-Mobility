package com.coppernic.mobility.data.result

import androidx.room.Embedded
import androidx.room.Relation
import com.coppernic.mobility.data.models.entities.ImageUser
import com.coppernic.mobility.data.models.entities.Marcacion

data class MarcacionWithImage(
    @Embedded val marcacion: Marcacion,
    @Relation(
        parentColumn = "guidUser",
        entityColumn = "userGui"
    )
    val cardImageUser: ImageUser
    )