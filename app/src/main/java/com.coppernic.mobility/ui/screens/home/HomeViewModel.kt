package com.coppernic.mobility.ui.screens.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.data.models.dao.CardholderDao
import com.coppernic.mobility.data.models.dao.CredentialDao
import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.data.models.dao.MarcacionDao
import com.coppernic.mobility.data.models.entities.Cardholder
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.data.models.entities.ImageUser
import com.coppernic.mobility.domain.interactors.UpdateCardHolders
import com.coppernic.mobility.domain.interactors.UpdateCredentials
import com.coppernic.mobility.domain.useCases.GetConnection
import com.coppernic.mobility.domain.util.*
import com.coppernic.mobility.util.Resource
import com.coppernic.mobility.util.interfaces.AppPreferences
import com.coppernic.mobility.util.interfaces.AppTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.Socket
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val updateCardHolders: UpdateCardHolders,
    private val updateCredentials: UpdateCredentials,
    private val marcacionDao: MarcacionDao,
    private val imageDao: ImageDao,
    private val cardholderDao: CardholderDao,
    private val getConnection: GetConnection,
    private val credentialDao: CredentialDao,
//    private val appUtil: AppUtil,
    appPreferences: AppPreferences,
    private val appTasks: AppTasks,
    @ApplicationContext private val context:Context
):ViewModel(){
    val tableName = appPreferences.tableNameStream
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val networkConnection = MutableStateFlow<NetworkStatus>(NetworkStatus.Unavailable)
    private val marcacionCount = MutableStateFlow(0)
//    private val changedNetworkStatus = context.networkStatus
////        .dropWhile { it == NetworkStatus.Available } // ignore initial available status
//        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    val state:StateFlow<HomeState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        networkConnection,
        marcacionCount,
//        observerMarcacionesCount.flow
    ){loading,message,connection,marcacionCount->
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
//        viewModelScope.launch {
//            changedNetworkStatus.collect{
//                this@HomeViewModel.networkConnection.emit(it)
//            }
//        }
//        insertToDb()
        getMarcarcaionCount()
//        connectToServer()
        viewModelScope.launch {
            do{
                   Log.d("DEBUG_D","try again")
              checkConnection()
                delay(2000)
            }while(isActive)
        }
//        Log.d("DEBUGG_DD", isPortInUse("http://10.0.1.181",12015).toString())
//        isPortInUse("http://10.0.1.181",12015)
    }

    private fun checkConnection(){
        viewModelScope.launch {
            getConnection().collect{result->
                when(result){
                    is Resource.Success->{
                        this@HomeViewModel.networkConnection.emit(NetworkStatus.Available)
                    }
                    is Resource.Error ->{
                        this@HomeViewModel.networkConnection.emit(NetworkStatus.Unavailable)
                    }
                    else -> {}
                }
            }
        }
    }

    fun insertToDb() {
        viewModelScope.launch(Dispatchers.IO) {
////            val credentials = ca
////
//            val imageCount = imageDao.getUserImages()
//            Log.d("DEBUG_D",imageCount.size.toString())
//            val credentials = credentialDao.getCredentialList()
//            val cardHolders = cardholderDao.getCardholder2()
            imageDao.insert(
                ImageUser(
                    userGui = "100aba66-4a9d-47fb-93b7-5a28eb06e365",
                    nombre = "Juan  Soliz ",
                    picture = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
                )
            )
            imageDao.insert(
                ImageUser(
                    userGui = "5",
                    nombre = "Ernesto Villa",
//                    picture = BitmapFactory.decodeResource(context.resources,R.drawable.profile)
                    picture = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
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
//                    id = 1000,
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
                    guid = "5",
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
//                    id = 1000,
                    guidCardHolder = "5",
                    guid = "ad8ca593-1ea1-4934-897d-15c1e197309b",
                    cardNumber = 1437,
                    facilityCode = 213,
                    uniqueId = "00000000000000000000000003AA11F3|40",
                    estado = "Active"
                )
            )
//            Log.d("CARD_HOLDER_RESULTS", "cardHolderDb $cardHolders")
//            Log.d("CREDENTIAL_RESULTS", "cardHolderDb $credentials")
//        }
        }
    }


    fun getMarcarcaionCount(){
//      observerMarcacionesCount(ObserverMarcacionesCount.Params(Unit))
        viewModelScope.launch {
            val count = marcacionDao.getMarcacionCount()
            this@HomeViewModel.marcacionCount.emit(count)
        }
    }



//    @SuppressLint("NewApi")
    fun updateCardHolders(){
        viewModelScope.launch {
            updateCardHolders(UpdateCardHolders.Params(true)).collectStatus(loadingCounter,uiMessageManager)
            updateCredentials(UpdateCredentials.Params(true)).collectStatus(loadingCounter,uiMessageManager)
            appTasks.sendMarcaciones()
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }



}