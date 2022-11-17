package com.coppernic.mobility.ui.screens.estadoPerson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.dto.mustering.DataX
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.domain.useCases.GetEstadoPerson
import com.coppernic.mobility.domain.util.*
import com.coppernic.mobility.domain.util.UiMessage
import com.coppernic.mobility.util.Resource
import com.coppernic.mobility.util.constants.Params

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class EstadoViewModel @Inject constructor(
    private val getEstadoPerson: GetEstadoPerson,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    val estadoPerson = savedStateHandle.get<String>(Params.ESTADO_PERSON_P)
    private val ciudadParam = savedStateHandle.get<String>(Params.CIUDAD_PARAM)
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager2()
    private val query = MutableStateFlow("")
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
                 delay(2000)
             }while(isActive)
            }
        }

        viewModelScope.launch {
            query.debounce(300).collect{
                updateData(it)
            }
        }

    }

    private fun  updateData(query:String){
        viewModelScope.launch {
        val results = musteringByZona.value.filter { it.nombre.lowercase().contains(query.lowercase()) }
            this@EstadoViewModel.musteringByZona.emit(results)
        }
    }



    private fun getMusteringByZoneFoo(idEstado:Int, ciudadId:Int,query:String){
        viewModelScope.launch {
//            getEstadoPerson(idEstado,ciudadId,query).collectData(loadingCounter,uiMessageManager,musteringByZona)
            getEstadoPerson(idEstado,ciudadId,query).collect{result->
                when(result){
                    is Resource.Error -> {
                        uiMessageManager.emitMessage(UiMessage(message = result.message?:"Error Inesperado"))
                    }
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

    fun clearMessage(){
        viewModelScope.launch {
            uiMessageManager.clearMessage()
        }
    }
    fun claerQuery(){
        viewModelScope.launch {
            this@EstadoViewModel.query.emit("")
        }
    }
}