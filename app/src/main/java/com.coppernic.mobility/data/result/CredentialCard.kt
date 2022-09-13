package com.coppernic.mobility.data.result

import androidx.room.Embedded
import androidx.room.Relation
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.data.models.entities.ImageUser

data class CredentialCard(
     @Embedded val cardImage:ImageUser,
     @Relation(
         parentColumn = "userGui",
         entityColumn = "guidCardHolder"
     )
     val credential: Credential
)