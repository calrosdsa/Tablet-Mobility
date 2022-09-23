package com.coppernic.mobility.ui.screens.accesssToConfig

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.models.dao.AccessDao
import com.coppernic.mobility.domain.util.UiMessage
import com.coppernic.mobility.domain.util.UiMessageManager
import com.coppernic.mobility.util.interfaces.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessConfigViewModel @Inject constructor(
    private val accessDao: AccessDao,
    appPreferences: AppPreferences,
):ViewModel(){
    private val uiMessageManager  = UiMessageManager()
    private val binaryCode = MutableStateFlow<Int?>(null)
    private val isAuthenticated = MutableStateFlow(false)
    private val accessCredentials = MutableStateFlow<List<Int>>(emptyList())
    val pin = appPreferences.password
    val state:StateFlow<AccessConfigState> = combine(
        uiMessageManager.message,
        binaryCode,
        isAuthenticated
    ){message,binaryCode,isAuthenticated->
        AccessConfigState(
            message = message,
            binaryCode = binaryCode,
            isAuthenticated = isAuthenticated
        )
    }.stateIn(
        scope =  viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccessConfigState()
    )

    init{
        viewModelScope.launch {
            accessDao.getAccess().collect{value->
                try {
                    val cards = value.accessSettings.map { it.cardNumber }
                    this@AccessConfigViewModel.accessCredentials.emit(cards)
                }catch (e:Exception){
//                    uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:"Error inesperado"))
                    this@AccessConfigViewModel.accessCredentials.emit(emptyList())
                }
            }
        }
        viewModelScope.launch {
            binaryCode.collect{
                if (it != null ) {
                    if(it in accessCredentials.value){
                  this@AccessConfigViewModel.isAuthenticated.emit(true)
                    }else{
                uiMessageManager.emitMessage(UiMessage("Tarjeta no autorizada: $it"))
                    }
                }
                this@AccessConfigViewModel.binaryCode.emit(null)
            }
        }
    }

    fun getCode(code:String,valueText: MutableState<String>) {
        val cardCode = code.substring(12).dropLast(2)
        viewModelScope.launch {
            if(cardCode.toBigIntegerOrNull() != null){
                this@AccessConfigViewModel.binaryCode.emit(cardCode.toInt(2))
                valueText.value = ""
            }else{
                delay(1800)
                valueText.value = ""
                uiMessageManager.emitMessage(UiMessage("Lectura incorrecta vuelva a escanear la tarjeta"))
            }
        }
    }


    fun clearMessage(id:Long){
        viewModelScope.launch {
        uiMessageManager.clearMessage(id)
        }
    }
    fun clearAuth(){
        viewModelScope.launch {
            this@AccessConfigViewModel.isAuthenticated.emit(false)
        }
    }


}