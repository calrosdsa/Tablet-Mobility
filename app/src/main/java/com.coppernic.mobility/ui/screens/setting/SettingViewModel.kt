package com.coppernic.mobility.ui.screens.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.models.dao.AccessDao
import com.coppernic.mobility.data.models.dao.ConfigDao
import com.coppernic.mobility.data.models.entities.AccessEntity
import com.coppernic.mobility.data.models.entities.Config
import com.coppernic.mobility.domain.interactors.UpdateCardHolders
import com.coppernic.mobility.domain.interactors.UpdateCredentials
import com.coppernic.mobility.domain.observers.ObserverSettings
import com.coppernic.mobility.domain.useCases.GetAccessInicio
import com.coppernic.mobility.domain.useCases.GetSettings
import com.coppernic.mobility.domain.util.ObservableLoadingCounter
import com.coppernic.mobility.domain.util.UiMessage
import com.coppernic.mobility.domain.util.UiMessageManager
import com.coppernic.mobility.domain.util.collectStatus
import com.coppernic.mobility.util.Resource
import com.coppernic.mobility.util.interfaces.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    observerSettings:ObserverSettings,
    private val updateCardHolders: UpdateCardHolders,
    private val updateCredentials: UpdateCredentials,
    private val getSettings: GetSettings,
    private val configDao: ConfigDao,
    private val getAccessInicio: GetAccessInicio,
    private val accessDao: AccessDao
):ViewModel() {
    //    var imei = mutableStateOf<String>("")
//    val activity = context as Activity
    private val urlServidor = MutableStateFlow<String>(appPreferences.urlServidor)
    private val loadingCounter = ObservableLoadingCounter()
    val pin= appPreferences.passwordStream
    val accessPin= appPreferences.accessPinStream
    private val uiMessageManager = UiMessageManager()
    val route_stream = appPreferences.initialScreenStream
    val state: StateFlow<SettingState> = combine(
        observerSettings.flow,
        urlServidor,
        loadingCounter.observable,
        uiMessageManager.message,
    ) { setting,url_servidor,loading,message ->
        SettingState(
            settingState = setting,
            url_servidor = url_servidor,
            loading= loading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingState()
    )

    init {
        observerSettings(ObserverSettings.Params(Unit))
    }

    fun executeAsync() {
        viewModelScope.launch {
            try {
                loadingCounter.addLoader()
                getAccessInicio().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            accessDao.deleteAll()
                            val values = result.data?.data
                            val access = result.data2?.data
                                accessDao.insert(
                                    AccessEntity(
                                        accessInicio = values?: emptyList(),
                                        accessSettings = access?: emptyList()
                                    )
                                )
                        }
                        else -> {}
                    }
                }

                getSettings().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.data?.let {
                                configDao.deleteConfig()
                                configDao.insertConfig(
                                    Config(
                                        id = it.id,
                                        coordenada = it.coordenadas,
                                        interfaz = it.interfas,
                                        riopass = it.passwordRio,
                                        riouser = it.usuarioRio,
                                        zonaHoraria = it.ZonaHoraria,
                                        url_controladora = it.ipControlador,
                                        url_servidor = "http://172.20.10.55:91",
                                        localePass = "129192",
                                        zonaPoligono = "Zona Poligono",
                                        imei = "869448030242433",
//                                    imei = result.message.toString(),
                                        ciudades = it.ciudades,
                                    )
                                )
                                appPreferences.accessPin = it.passwordInicioApp
                                appPreferences.tableName = it.nombreTablet
                                appPreferences.password = it.passwordSettingsApp
                            }
                        }
                        else -> {}
                    }
                }
//                updateCardHolders(UpdateCardHolders.Params(true)).collectStatus(
//                    loadingCounter,
//                    uiMessageManager
//                )
//                updateCredentials(UpdateCredentials.Params(true)).collectStatus(
//                    loadingCounter,
//                    uiMessageManager
//                )
                uiMessageManager.emitMessage(UiMessage(message = "Datos Sincronizados"))
                loadingCounter.removeLoader()
            }catch (e:IOException){
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:"Error inesperado"))
            }

        }
    }

    fun setUrlServidor(pass: String){
        appPreferences.urlServidor = pass
    }

    fun setPassword(pass: String) {
        appPreferences.password = pass
    }
    fun setPinAccess(pin:String){
        appPreferences.accessPin = pin
    }
    fun setInitialScreen(screen :String){
        appPreferences.initialScreen = screen
    }
    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
