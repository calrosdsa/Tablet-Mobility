package com.coppernic.mobility.ui.screens.manual_marker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.domain.observers.ObserverCardCredential
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.util.constants.Params
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ManualMarkerViewModel @Inject constructor(
    observerCardCredential: ObserverCardCredential,
//    observerCardHolders: ObserverCardHolders,
    savedStateHandle: SavedStateHandle,
):ViewModel() {
    val pagingItems = observerCardCredential.flow.cachedIn(viewModelScope)
    val query = MutableStateFlow("")
    private val userChoice = savedStateHandle.get<String>(Params.TYPE_ACCESS_PARAM)
    private val isConsulta = savedStateHandle.get<String>(Params.IS_CONSULTA)
//    val state:StateFlow<ManualMarkerState> = combine(
//        query,
//        observerCardCredential.flow.debounce(400) ,
////        observerCardHolders.flow,
//    ){query,cardCredential->
//        ManualMarkerState(
//            query = query,
////            cardholder = cardHolders
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = ManualMarkerState()
//    )

    init{
        viewModelScope.launch {
            query.debounce(300).collect{
                observerCardCredential(ObserverCardCredential.Params(
                    pagingConfig = PAGING_CONFIG,
                    query = it
                ))
            }

//                query.debounce(300)
//                .onEach {query->
//                    val job = launch {
//                        observerCardCredential(ObserverCardCredential.Params(query))
//                    }
//                    job.join()
//                }.catch {
//
//                }.collect()
        }
    }

    fun sendManualConfirmation(cardCredential: Credential, navController: NavController){
        if(isConsulta == "0"){
            navController.navigate(MainDestination.CONSULTA_SCREEN + "?${Params.TYPE_ACCESS_PARAM}=${userChoice}" +
                    "&facility_code=${cardCredential.facilityCode}&card_number=${cardCredential.cardNumber}"
            ){
//             popUpTo(573383227)
                popUpTo(-240764362)
//             popUpTo(1665011537)
            }
        }else{

         navController.navigate(MainDestination.WAITING_SCREEN + "?${Params.TYPE_ACCESS_PARAM}=${userChoice}" +
                 "&facility_code=${cardCredential.facilityCode}&card_number=${cardCredential.cardNumber}"
         ){
//             popUpTo(573383227)
             popUpTo(-240764362)
//             popUpTo(1665011537)
         }
        }
    }

    fun search(searchTerm: String) {
        viewModelScope.launch {
//            Log.d("SEARCH_STATE", searchTerm)
            this@ManualMarkerViewModel.query.emit(searchTerm)
        }
    }


    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        )
    }

}