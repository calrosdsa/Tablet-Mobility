package com.coppernic.mobility.ui.screens.waitingAccess

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.coppernic.mobility.domain.util.UiMessage

data class AccessState(
    val personAccess:AccessPerson? = null,
    val userChoice:String? = null,
    val message: UiMessage? = null,
    val binaryCode:String? = null
)


data class AccessPerson(
    val personName:String? = null,
    val cardNumber:Int? = null,
    val empresa:String? = null,
    val ci:String? = null,
    val picture:String? = "",
    val stateBackGround: Color? = null,
    val accessState:String? = null,
    val accessDetail:String? = null,
)
