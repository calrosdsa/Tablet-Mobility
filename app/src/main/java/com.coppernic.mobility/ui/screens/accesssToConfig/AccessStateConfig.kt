package com.coppernic.mobility.ui.screens.accesssToConfig

import com.coppernic.mobility.domain.util.UiMessage

data class AccessConfigState(
    val message:UiMessage? = null,
    val binaryCode:Int? = null,
    val isAuthenticated:Boolean = false,
    val accessValue: List<Int> = emptyList()
)
