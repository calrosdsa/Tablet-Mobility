package com.coppernic.mobility.ui.screens.ciudades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.dto.settings.Ciudade
import com.coppernic.mobility.domain.observers.ObserverSettings
import com.coppernic.mobility.domain.util.UiMessage
import com.coppernic.mobility.domain.util.UiMessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CiudadesVIiewModel @Inject constructor(
    private val observerSettings: ObserverSettings
) :ViewModel(){
//    var state = mutableStateOf<List<Ciudade>>(emptyList())
    private val ciudades = MutableStateFlow<List<Ciudade>>(emptyList())
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<CiudadState> = combine(
        ciudades,
        uiMessageManager.message
    ){ciudades,message->
        CiudadState(
            ciudades = ciudades,
            message = message
        )
    }.stateIn(
        scope= viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CiudadState()
    )
    init{
          observerSettings(ObserverSettings.Params(Unit))
          getCiudades()
    }

    fun getCiudades(){
        viewModelScope.launch {
                try {
            observerSettings.flow.collect{
                    this@CiudadesVIiewModel.ciudades.emit(it.ciudades)
            }
                }catch (e:Throwable){
                    this@CiudadesVIiewModel.ciudades.emit(emptyList())
//                    if(ciudades.value.isEmpty())  UiMessage(message = "No hay datos disponibles")
//                    uiMessageManager.emitMessage(
                        UiMessage(
                            message = (e.localizedMessage?: "Un error Inesperado")
                        )
//                    )
                }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

}