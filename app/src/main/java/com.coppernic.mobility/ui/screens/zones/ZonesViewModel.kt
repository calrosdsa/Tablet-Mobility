package com.coppernic.mobility.ui.screens.zones

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.domain.useCases.GetMusteringByZone
import com.coppernic.mobility.domain.util.ObservableLoadingCounter
import com.coppernic.mobility.domain.util.UiMessageManager
import com.coppernic.mobility.domain.util.collectData
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
    private val musteringByZona = MutableStateFlow<MusteringByZonaDto?>(null)
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
            getMusteringByZoneFoo(zoneParam.toInt(),ciudadParam.toInt())
                 delay(5000)
             }while(isActive)
            }
        }

    }


    fun getMusteringByZoneFoo(zoneId:Int, ciudadId:Int){
        viewModelScope.launch {
            getMusteringByZone(zoneId,ciudadId).collectData(loadingCounter,uiMessageManager,musteringByZona)
        }
    }
}