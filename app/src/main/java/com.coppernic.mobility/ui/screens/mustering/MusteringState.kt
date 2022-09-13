package com.coppernic.mobility.ui.screens.mustering

import com.coppernic.mobility.data.dto.mustering.MusteringByCiudadDto
import com.coppernic.mobility.domain.util.UiMessage

data class MusteringState(
    val totalCount : DataResultFloat? = null,
    val musteringByCiudad: MusteringByCiudadDto? = null,
    val loading:Boolean = false,
    val uiMessage: UiMessage? = null
)


data class DataResult(
    val total:Int,
    val seguros:Int,
    val inseguros:Int
)

data class DataResultFloat(
    val seguros:Float? = null,
    val inseguros: Float? = null
)