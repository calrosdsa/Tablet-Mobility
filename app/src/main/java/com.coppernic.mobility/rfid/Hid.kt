package com.coppernic.mobility.rfid

interface Hid {
    var listeners:MutableList<RfidListener>
    var hidIclassInteractor:HidIclassInteractor

    fun subscribe(l:RfidListener)

    fun unsubscribe(l:RfidListener)

    fun setUp()

    fun read()

    fun dispose()

}