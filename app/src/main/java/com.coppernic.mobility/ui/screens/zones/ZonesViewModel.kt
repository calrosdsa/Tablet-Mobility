package com.coppernic.mobility.ui.screens.zones

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.dto.mustering.DataX
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.domain.useCases.GetMusteringByZone
import com.coppernic.mobility.domain.util.*
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
class ZonesViewModel @Inject constructor(
    private val getMusteringByZone: GetMusteringByZone,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val zoneParam = savedStateHandle.get<String>(Params.ZONE_PARAM)
    private val ciudadParam = savedStateHandle.get<String>(Params.CIUDAD_PARAM)
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager2()
    private val query = MutableStateFlow("")
    private val musteringByZona = MutableStateFlow<List<DataX>>(emptyList())
    val state :StateFlow<ZoneState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        musteringByZona
    ){loading,message,musteringZone->
        ZoneState(
            loading = loading,
            uiMessage = message,
            zone  = musteringZone
        )
    }.stateIn(
        scope=  viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ZoneState()
    )

    init{
        if(zoneParam != null && ciudadParam != null){
            viewModelScope.launch {
             do{
            getMusteringByZoneFoo(zoneParam.toInt(),ciudadParam.toInt(),query.value)
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
            this@ZonesViewModel.musteringByZona.emit(results)
        }
    }

    fun search(query:String){
        viewModelScope.launch {
            this@ZonesViewModel.query.emit(query)
        }
    }

    private fun getMusteringByZoneFoo(zoneId:Int, ciudadId:Int,query: String){
        viewModelScope.launch {
//            getMusteringByZone(zoneId,ciudadId).collectData(loadingCounter,uiMessageManager,musteringByZona)
            getMusteringByZone(zoneId,ciudadId,query).collect{result->
                when(result){
                    is Resource.Error -> {
                        result.message?.let { UiMessage(it) }
                            ?.let { uiMessageManager.emitMessage(it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        result.data?.let { this@ZonesViewModel.musteringByZona.emit(it) }
                    }
                }
            }
        }
    }
    fun clearQuery(){
        viewModelScope.launch {
            this@ZonesViewModel.query.emit("")
        }
    }

    fun clearMessage(){
        viewModelScope.launch {
            uiMessageManager.clearMessage()
        }
    }
}