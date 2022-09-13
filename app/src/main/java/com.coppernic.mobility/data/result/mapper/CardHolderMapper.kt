package com.coppernic.mobility.data.result.mapper

import com.coppernic.mobility.data.dto.cardHolder.Data
import com.coppernic.mobility.data.dto.credentials.CredentialDto
import com.coppernic.mobility.data.models.entities.Cardholder
import com.coppernic.mobility.data.models.entities.Credential

fun Data.toCardHolderEntity(): Cardholder {
    return Cardholder(
         guid = guid,
//   firstName = firstName,
//    lastName = lastName,
     ci = ci,
    descriptions = descriptions,
    empresa = empresa,
        estado = estado,
    )
}

fun CredentialDto.toCrendentialEntity(): Credential {
    return Credential(
//        var fecha: String?,
     guid = guid,
     guidCardHolder = guidCardHolder,
     cardNumber = cardNumber,
     facilityCode = facilityCode,
     uniqueId = uniqueId,
    estado = estado ,
    )
}