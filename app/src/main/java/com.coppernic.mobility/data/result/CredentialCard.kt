package com.coppernic.mobility.data.result

import androidx.room.Embedded
import androidx.room.Relation
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.data.models.entities.ImageUser
import com.coppernic.mobility.data.models.entities.Marcacion

data class CredentialCard(
    @Embedded val credential: Credential,
    @Relation(
        parentColumn = "guidCardHolder",
        entityColumn = "userGui"
    )
    val cardImage: ImageUser
)