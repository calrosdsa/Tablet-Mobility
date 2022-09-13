package com.coppernic.mobility.data.models.mustering

data class MusteringGrafico(
    val cantidadSeguros: Int,
    val cantidadInseguros: Int,
    val total: Int,
    val zonas: List<MusteringZona>
)
