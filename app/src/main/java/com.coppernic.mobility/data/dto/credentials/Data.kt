package com.coppernic.mobility.data.dto.credentials

data class CredentialDto(
    val cardNumber: Int,
    val estado: String,
    val facilityCode: Int,
    val guid: String,
    val guidCardHolder: String,
    val uniqueId: String
)