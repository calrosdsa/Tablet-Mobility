package com.coppernic.mobility.data.dto.credentials

data class Credentials(
    val data: List<CredentialDto>,
    val message: Any,
    val result: Int
)