package com.coppernic.mobility.ui.screens.accesss

import com.coppernic.mobility.data.dto.access.AccessValue
import com.coppernic.mobility.domain.util.UiMessage

data class InitialState(
    val message:UiMessage? = null,
    val binaryCode:Int? = null,
    val isAuthenticated:Boolean = false,
    val accessValue: List<Int> = emptyList()
)
