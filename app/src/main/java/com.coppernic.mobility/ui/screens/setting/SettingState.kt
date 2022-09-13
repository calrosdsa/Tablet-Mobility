package com.coppernic.mobility.ui.screens.setting

import com.coppernic.mobility.data.models.entities.Config
import com.coppernic.mobility.domain.util.UiMessage

data class SettingState (
    val settingState:Config? = null,
    val url_servidor:String = "",
    val loading:Boolean = false,
    val message: UiMessage? = null
)