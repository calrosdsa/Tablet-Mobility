package com.coppernic.mobility.ui.screens.home

import com.coppernic.mobility.domain.util.NetworkStatus
import com.coppernic.mobility.domain.util.UiMessage

data class HomeState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val passwordPref:String= "",
    val netWorkConnection:NetworkStatus= NetworkStatus.Unavailable,
    val marcacionCount:Int = 0
)