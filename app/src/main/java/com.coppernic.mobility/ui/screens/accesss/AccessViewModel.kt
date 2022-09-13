package com.coppernic.mobility.ui.screens.accesss

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
//    private val presenter :Hid,
    @ApplicationContext private val context: Context
):ViewModel(){

//    init{
//        presenter.subscribe(this)
//    }
//
//    fun onStart(){
//         presenter.read()
//        Toast.makeText(context, "acerque la tarjeta para leer", Toast.LENGTH_SHORT).show()
//    }
//
//    fun onStop(){
//        presenter.unsubscribe(this)
//    }
//
//
//
//    override fun showWaiting() {
//        TODO("Not yet implemented")
//    }
//
//    override fun showError(message: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun cardReaded(card: Card) {
//        TODO("Not yet implemented")
//
//    }
}