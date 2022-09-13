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
import com.coppernic.mobility.rfid.RfidListener
import com.coppernic.mobility.util.Resource
import com.coppernic.mobility.util.interfaces.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.coppernic.sdk.hid.iclassProx.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val updateCardHolders: UpdateCardHolders,
    private val updateCredentials: UpdateCredentials,
    private val credentialDao: CredentialDao,
    private val cardholderDao: CardholderDao,
    private val imageDao: ImageDao,
    private val appUtil: AppUtil,
    private val configDao: ConfigDao,
    private val observerMarcacionesCount: ObserverMarcacionesCount,
    private val getSettings: GetSettings,
    @ApplicationContext private val context:Context
):ViewModel(), RfidListener {
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


    fun updateCredentials(){
        viewModelScope.launch {
            updateCredentials(UpdateCredentials.Params(true)).collectStatus(loadingCounter,uiMessageManager)
        }
    }


    fun insertToDb() {
        viewModelScope.launch(Dispatchers.IO) {
////            val credentials = ca
////
            val credentials = credentialDao.getCredentialList()
            val cardHolders = cardholderDao.getCardholder2()
            imageDao.insert(
                ImageUser(
                    userGui = "100aba66-4a9d-47fb-93b7-5a28eb06e365",
                    nombre = "Juan Soliz ",
                    picture = appUtil.getImageBitmap(context,"https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png")
                )
            )
            imageDao.insert(
                ImageUser(
                    userGui = "305aba66-4a9d-47fb-93b7-5a28eb06e365",
                    nombre = "Diego Acosta",
                    picture = appUtil.getImageBitmap(context,"https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png")
                )
            )
        cardholderDao.insert(
            Cardholder(
            guid = "100aba66-4a9d-47fb-93b7-5a28eb06e365",
//            firstName = "Pedro Alberto",
//            lastName = "Soliz Gallardo",
            ci = "89543216 SC",
            descriptions = "21k2 121sasas",
            empresa = "TECLU ",
            estado = "Inactive",
        )
        )
            credentialDao.insert(
                Credential(
                    guidCardHolder = "100aba66-4a9d-47fb-93b7-5a28eb06e365",
                    guid = "cb8ca593-1ea1-4934-897d-15c1e197309b",
                    cardNumber = 2937,
                    facilityCode = 213,
                    uniqueId = "00000000000000000000000003AA16F3|26",
                    estado = "Inactive"
                )
            )
            cardholderDao.insert(
                Cardholder(
                guid = "305aba66-4a9d-47fb-93b7-5a28eb06e365",
//                firstName = "Juan Marcos",
//                lastName = "Medina Fuentes",
                ci = "89543216 SC",
                descriptions = "21k2 121sasas",
                empresa = "TECLU ",
                estado = "Active",
            )
            )
            credentialDao.insert(
                Credential(
                    guidCardHolder = "305aba66-4a9d-47fb-93b7-5a28eb06e365",
                    guid = "ad8ca593-1ea1-4934-897d-15c1e197309b",
                    cardNumber = 1137,
                    facilityCode = 123,
                    uniqueId = "00000000000000000000000003AA11F3|40",
                    estado = "Active"
                )
            )
            Log.d("CARD_HOLDER_RESULTS", "cardHolderDb $cardHolders")
            Log.d("CREDENTIAL_RESULTS", "cardHolderDb $credentials")

//        }
        }
    }






    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    override fun showWaiting() {
        Toast.makeText(context, "En espera", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(context, "Un error a ocurrido", Toast.LENGTH_SHORT).show()
    }

    override fun cardReaded(card: Card) {
        TODO("Not yet implemented")
    }

}