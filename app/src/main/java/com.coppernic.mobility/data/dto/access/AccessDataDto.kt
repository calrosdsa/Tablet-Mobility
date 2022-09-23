package com.coppernic.mobility.data.dto.access

data class AccessDataDto(
    val data: List<AccessValue>,
    val message: Any,
    val result: Int
)