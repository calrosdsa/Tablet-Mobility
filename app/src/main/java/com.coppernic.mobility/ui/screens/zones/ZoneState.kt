package com.coppernic.mobility.ui.screens.zones

import com.coppernic.mobility.data.dto.mustering.DataX
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.domain.util.UiMessage

data class ZoneState (
    val loading:Boolean = false,
    val uiMessage: UiMessage? = null,
    val zone:List<DataX> = emptyList()
        )