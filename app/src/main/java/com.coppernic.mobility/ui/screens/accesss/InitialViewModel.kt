package com.coppernic.mobility.ui.screens.accesss

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
class InitialViewModel @Inject constructor(
    private val accessDao: AccessDao,
    appPreferences: AppPreferences,
):ViewModel(){
    private val uiMessageManager  = UiMessageManager()
    private val binaryCode = MutableStateFlow<Int?>(null)
    private val isAuthenticated = MutableStateFlow(false)
    private val accessCredentials = MutableStateFlow<List<Int>>(emptyList())
    val pin = appPreferences.password
    val accessPin = appPreferences.accessPin
    val state:StateFlow<InitialState> = combine(
        uiMessageManager.message,
        binaryCode,
        isAuthenticated
    ){message,binaryCode,isAuthenticated->
        InitialState(
            message = message,
            binaryCode = binaryCode,
            isAuthenticated = isAuthenticated
        )
    }.stateIn(
        scope =  viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InitialState()
    )

    init{
        viewModelScope.launch {
            accessDao.getAccess().collect{value->
                try {
                    val cards = value.accessInicio.map { it.cardNumber }
                    this@InitialViewModel.accessCredentials.emit(cards)
                }catch (e:Exception){
//                    uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:"Error inesperado"))
                    this@InitialViewModel.accessCredentials.emit(emptyList())
                }
            }
        }
        viewModelScope.launch {
            binaryCode.collect{
                if (it != null ) {
                    if(it in accessCredentials.value){
                  this@InitialViewModel.isAuthenticated.emit(true)
                    }else{
                uiMessageManager.emitMessage(UiMessage("Tarjeta no autorizada: $it"))
                    }
                }
                this@InitialViewModel.binaryCode.emit(null)
            }
        }
    }

    fun getCode(code:String,valueText: MutableState<String>) {
        val cardCode = code.substring(12).dropLast(2)
        viewModelScope.launch {
            if(cardCode.toBigIntegerOrNull() != null){
                this@InitialViewModel.binaryCode.emit(cardCode.toInt(2))
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


}