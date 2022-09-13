package com.coppernic.mobility.data.dto.mustering

data class Data(
    val cantidadInseguros: Int,
    val cantidadSeguros: Int,
    val total: Int,
    val zonas: List<Zona>
)