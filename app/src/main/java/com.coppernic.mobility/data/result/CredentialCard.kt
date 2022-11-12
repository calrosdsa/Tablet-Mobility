package com.coppernic.mobility.data.result

import androidx.room.Embedded
import androidx.room.Relation
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.data.models.entities.ImageUser
import com.coppernic.mobility.data.models.entities.Marcacion
import java.util.*

class CredentialCard {
    @Embedded
    var credential: Credential? = null

    @Relation(
        parentColumn = "guidCardHolder",
        entityColumn = "userGui"
    )
    var cardImage: ImageUser? = null

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is CredentialCard -> credential == other.credential && cardImage == other.cardImage
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(credential, cardImage)

}
