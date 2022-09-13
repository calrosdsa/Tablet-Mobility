package com.coppernic.mobility.ui.screens.manual_marker

import com.coppernic.mobility.data.models.entities.Cardholder
import com.coppernic.mobility.data.result.CredentialCard

data class ManualMarkerState(
    val query:String ="",
//    val cardCredential: List<CardCredential> = emptyList(),
    val cardCredential: List<CredentialCard> = emptyList(),
    val cardholder: List<Cardholder>? = null
)


object CardCreditPerson {
    const val firstName: String = "Miguel Andres"
    const val lastName: String = "Martinez Vega"
    const val facilityCode: Int = 120102
    const val cardNumber: Int = 2102101
    const val guidCardHolder: String = "1212112"
    const val estado: String = "Activo"
    const val ci: Int = 2119912
//    val picture: Bitmap?,
}