package com.coppernic.mobility.rfid

import fr.coppernic.sdk.hid.iclassProx.Card

interface RfidListener {
    fun showWaiting()
    fun showError(message: String)
    fun cardReaded(card: Card)
}