package com.coppernic.mobility.util.interfaces

import kotlinx.coroutines.flow.StateFlow

interface AppPreferences {
    val passwordStream:StateFlow<String>
    var password:String
    val urlServidorStream:StateFlow<String>
    var urlServidor:String
}