package com.coppernic.mobility.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.models.dao.*
import com.coppernic.mobility.data.models.entities.Cardholder
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.data.models.entities.Config
import com.coppernic.mobility.data.models.entities.ImageUser
import com.coppernic.mobility.domain.interactors.UpdateCardHolders
import com.coppernic.mobility.domain.interactors.UpdateCredentials
import com.coppernic.mobility.domain.observers.ObserverMarcacionesCount
import com.coppernic.mobility.domain.useCases.GetSettings
import com.coppernic.mobility.domain.util.*
import com.coppernic.mobility.util.Resource
import com.coppernic.mobility.util.interfaces.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val updateCardHolders: UpdateCardHolders,
    private val updateCredentials: UpdateCredentials,
    private val configDao: ConfigDao,
    private val observerMarcacionesCount: ObserverMarcacionesCount,
    private val getSettings: GetSettings,
    @ApplicationContext private val context:Context
):ViewModel(){
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val networkConnection = MutableStateFlow<NetworkStatus>(NetworkStatus.Unavailable)
    private val changedNetworkStatus = context.networkStatus
//        .dropWhile { it == NetworkStatus.Available } // ignore initial available status
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    val state:StateFlow<HomeState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        networkConnection,
        observerMarcacionesCount.flow
    ){loading,message,connection, marcacionCount->
       HomeState(
           loading = loading,
           message = message,
           netWorkConnection = connection,
           marcacionCount = marcacionCount
       )
    }.stateIn(
        scope = viewModelScope,
        initialValue = HomeState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    init{
        viewModelScope.launch {
            changedNetworkStatus.collect{
                this@HomeViewModel.networkConnection.emit(it)
            }
        }
//        viewModelScope.launch {
//        insertToDb()
        getMarcarcaionCount()


//        deleteMarcaciones()
//        init()
    }


    fun getMarcarcaionCount(){
      observerMarcacionesCount(ObserverMarcacionesCount.Params(Unit))
    }



    @SuppressLint("NewApi")
    fun updateCardHolders(){
        viewModelScope.launch {
            val telephoneManger =  context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            updateCardHolders(UpdateCardHolders.Params(true)).collectStatus(loadingCounter,uiMessageManager)
            updateCredentials(UpdateCredentials.Params(true)).collectStatus(loadingCounter,uiMessageManager)
            getSettings().collect{result->
                when(result){
                    is Resource.Success -> {
                        result.data?.data?.let {
                            configDao.deleteConfig()
                            configDao.insertConfig(
                                Config(
                                    id=it.id,
                                    coordenada = it.coordenadas,
                                    interfaz = it.interfas,
                                    riopass = it.passwordRio,
                                    riouser = it.usuarioRio,
                                    zonaHoraria = it.ZonaHoraria,
                                    url_controladora = it.ipControlador,
                                    url_servidor = "http://172.20.10.55:91",
                                    localePass ="129192",
                                    zonaPoligono = "Zona Poligono",
                                    imei = telephoneManger.imei,
                                    ciudades = it.ciudades
                                )
                            )
                        }
                    }
                    is Resource.Error ->{
                        uiMessageManager.emitMessage(UiMessage(message = result.message?:"Ha ocurrido un error desconocido"))
                    }
//                    Result.retry()
                    else -> {}
                }
            }
//            updateCredentials(UpdateCredentials.Params("asas",true))
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }



}