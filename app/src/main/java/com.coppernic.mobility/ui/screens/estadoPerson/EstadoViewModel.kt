package com.coppernic.mobility.ui.screens.estadoPerson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.dto.mustering.DataX
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.domain.useCases.GetEstadoPerson
import com.coppernic.mobility.domain.util.ObservableLoadingCounter
import com.coppernic.mobility.domain.util.UiMessageManager
import com.coppernic.mobility.domain.util.collectData
import com.coppernic.mobility.util.Resource
import com.coppernic.mobility.util.constants.Params

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadoViewModel @Inject constructor(
    private val getEstadoPerson: GetEstadoPerson,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    val estadoPerson = savedStateHandle.get<String>(Params.ESTADO_PERSON_P)
    val ciudadParam = savedStateHandle.get<String>(Params.CIUDAD_PARAM)
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val query = MutableStateFlow<String>("")
    private val musteringByZona = MutableStateFlow<List<DataX>>(emptyList())
    val state :StateFlow<PersonState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        musteringByZona
    ){loading,message,musteringZone->
        PersonState(
            loading = loading,
            uiMessage = message,
            zone  = musteringZone
        )
    }.stateIn(
        scope=  viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PersonState()
    )

    init{
        if(estadoPerson != null && ciudadParam != null){
            viewModelScope.launch {
             do{
            getMusteringByZoneFoo(estadoPerson.toInt(),ciudadParam.toInt(),query.value)
                 delay(5000)
             }while(isActive)
            }
        }

    }



    fun getMusteringByZoneFoo(idEstado:Int, ciudadId:Int,query:String){
        viewModelScope.launch {
//            getEstadoPerson(idEstado,ciudadId,query).collectData(loadingCounter,uiMessageManager,musteringByZona)
            getEstadoPerson(idEstado,ciudadId,query).collect{result->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        result.data?.let { this@EstadoViewModel.musteringByZona.emit(it) }
                    }
                }
            }
        }
    }

    fun search(query: String){
        viewModelScope.launch {
            this@EstadoViewModel.query.emit(query)
        }
    }
}