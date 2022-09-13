package com.coppernic.mobility.ui.screens.ciudades

import com.coppernic.mobility.data.dto.settings.Ciudade
import com.coppernic.mobility.domain.util.UiMessage

data class CiudadState (
    val ciudades:List<Ciudade> = emptyList(),
    val message: UiMessage? = null
        )