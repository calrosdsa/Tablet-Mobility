package com.coppernic.mobility.util

sealed class Resource<T>(val data: T?= null , val message: String? = null){
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String,data: T?= null): Resource<T>(data,message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

//Access State
//Access Detail
//Background

sealed class Access(){
    data class Accepted(
        val accessState: String,
        val accessDetail: String,
        val backGround: androidx.compose.ui.graphics.Color
    ):Access()
    data class Denied(
        val accessState: String,
        val accessDetail: String,
        val backGround: androidx.compose.ui.graphics.Color
    ):Access()
    data class Error(
        val accessState: String,
        val accessDetail: String,
        val backGround: androidx.compose.ui.graphics.Color
    ):Access()
}
