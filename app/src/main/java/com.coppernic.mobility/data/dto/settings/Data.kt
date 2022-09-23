package com.coppernic.mobility.data.dto.settings

data class Data(
    val ZonaHoraria: String,
    val ciudades: List<Ciudade>,
    val coordenadas: List<Coordenada>,
    val id: Int,
    val interfas: String,
    val ipControlador: String,
    val passwordRio: String,
    val usuarioRio: String,
    val passwordInicioApp:String,
    val passwordSettingsApp:String,
    val nombreTablet:String,
)