package com.coppernic.mobility.ui.screens.zones

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.dto.mustering.DataX
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.domain.useCases.GetMusteringByZone
import com.coppernic.mobility.domain.util.ObservableLoadingCounter
import com.coppernic.mobility.domain.util.UiMessage
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
class ZonesViewModel @Inject constructor(
    private val getMusteringByZone: GetMusteringByZone,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    val zoneParam = savedStateHandle.get<String>(Params.ZONE_PARAM)
    val ciudadParam = savedStateHandle.get<String>(Params.CIUDAD_PARAM)
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
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
                 delay(3000)
             }while(isActive)
            }
        }

    }

    fun search(query:String){
        viewModelScope.launch {
            this@ZonesViewModel.query.emit(query)
        }
    }

    fun getMusteringByZoneFoo(zoneId:Int, ciudadId:Int,query: String){
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
}