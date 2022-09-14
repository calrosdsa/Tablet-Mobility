package com.coppernic.mobility.ui.screens.consulta

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppernic.mobility.data.models.dao.CardholderDao
import com.coppernic.mobility.data.models.dao.CredentialDao
import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.domain.util.UiMessage
import com.coppernic.mobility.domain.util.UiMessageManager
import com.coppernic.mobility.ui.screens.waiting.AccessPerson
import com.coppernic.mobility.ui.screens.waiting.AccessState
import com.coppernic.mobility.util.Access
import com.coppernic.mobility.util.constants.Params
import com.coppernic.mobility.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConsultaViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val cardholderDao: CardholderDao,
        private val credentialDao: CredentialDao,
        private val imageDao: ImageDao,
        @ApplicationContext private val context: Context,
    ): ViewModel() {
        private val typeAccess = savedStateHandle.get<String>(Params.TYPE_ACCESS_PARAM)
        private val facilityCodeP = savedStateHandle.get<String>(Params.FACILITY_CODE_P)
        private val cardNumberP = savedStateHandle.get<String>(Params.CARD_NUMBER_P)
        private val uiMessageManager = UiMessageManager()
        private val userChoices = MutableStateFlow<String?>(null)
        private val accessPerson = MutableStateFlow(AccessPerson())
    private val binaryCode = MutableStateFlow<String?>(null)

        val state: StateFlow<AccessState> = combine(
            accessPerson,
            userChoices,
            uiMessageManager.message,
            binaryCode
        ){accessPerson,userChoice, message, binaryCode->
            AccessState(
                personAccess = accessPerson,
                userChoice = userChoice,
                message = message,
                binaryCode = binaryCode
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AccessState()
        )

        init{

//        presenter.setUp()
            viewModelScope.launch {
                userChoices.emit(typeAccess)
//            Log.d("ACCESS_STATE", "$facilityCodeP , $cardNumberP")
                if(facilityCodeP != null && cardNumberP != null){
                    checkValidation(facilityCodeP.toInt(),cardNumberP.toInt())
                }
            }
            viewModelScope.launch {
                accessPerson
//                .dropWhile { it.personName == "csa" }
//                .distinctUntilChanged()
                    .collectLatest{
                        delay(5000)
                        accessPerson.emit(AccessPerson())
                    }
            }
            viewModelScope.launch {
                binaryCode.collect{
                    if (it != null) {
                        checkValidation(facilityCode = 213, cardNumber = it.toInt(2))
                        this@ConsultaViewModel.binaryCode.emit(null)
                    }
                }
            }
        }
    fun getCode(code:String){
        val cardCode = code.substring(12).dropLast(2)
        viewModelScope.launch {
            this@ConsultaViewModel.binaryCode.emit(cardCode)
        }
    }


        fun checkValidation(facilityCode:Int,cardNumber:Int) {
            viewModelScope.launch(Dispatchers.IO) {
                val credential = credentialDao.getCredentialByNumber(facilityCode, cardNumber)
                val cardHolder = credential?.guidCardHolder?.let { cardholderDao.getCardholderByGuid(it) }
                val imageUser  = credential?.guidCardHolder?.let { imageDao.getUserImage(it) }
                if (credential == null) {
//                this@WaitingViewModel.accessPerson.emit(AccessPerson())
                    uiMessageManager.emitMessage(UiMessage("Credencial No registrada "))
//                        Toast.makeText(context,"No registrada en la base de datos",Toast.LENGTH_SHORT).show()
//                return@launch
                }
                flow<Access> {
                    try {
                        if (cardHolder?.estado == "Active") {
                            if (credential.estado == "Active") {
                                emit(Access.Accepted("Acceso Permitido", "", Color(0xFF00C853)))
                            } else {
                                val detail = when (credential.estado) {
                                    "Expired" -> "Tarjeta Inactiva"
                                    "Stolen" -> "Tarjeta Robada"
                                    "Lost" -> "Tarjeta Perdida"
                                    else -> "Tarjeta Desconocido"
                                }
                                emit(Access.Denied("Acceso Denegado", detail, Color.Red))
                            }
                        } else {
                            emit(Access.Denied("Acceso Denegado", "Usuario Inactivo", Color.Red))
                        }
                    } catch (e: NullPointerException) {
                        emit(Access.Error("Acceso Denegado", "Error Desconocido", Color.DarkGray))
                    }
                }.collect {
                    when (it) {
                        is Access.Accepted -> {
//                        playSound(true)
                            this@ConsultaViewModel.accessPerson.emit(
                                AccessPerson(
                                    personName = imageUser?.nombre,
                                    cardNumber = cardNumber,
                                    empresa = cardHolder?.empresa,
                                    ci = cardHolder?.ci,
                                    stateBackGround = it.backGround,
                                    accessDetail = it.accessDetail,
                                    accessState = it.accessState,
                                    picture = imageUser?.picture,
                                )
                            )
                        }
                        is Access.Denied -> {
                        playSound(false)
                            this@ConsultaViewModel.accessPerson.emit(
                                AccessPerson(
                                    personName = imageUser?.nombre,
                                    cardNumber = cardNumber,
                                    empresa = cardHolder?.empresa,
                                    ci = cardHolder?.ci,
                                    stateBackGround = it.backGround,
                                    accessDetail = it.accessDetail,
                                    accessState = it.accessState,
                                    picture = imageUser?.picture,
                                )
                            )
                        }
                        is Access.Error -> {
                        playSound(false)
                            this@ConsultaViewModel.accessPerson.emit(
                                AccessPerson(
                                    personName = imageUser?.nombre,
                                    cardNumber = cardNumber,
                                    empresa = cardHolder?.empresa,
                                    ci = cardHolder?.ci,
                                    stateBackGround = it.backGround,
                                    accessDetail = it.accessDetail,
                                    accessState = it.accessState,
                                    picture = imageUser?.picture,
                                )
                            )

                        }
                    }
                }
            }
        }


        private fun playSound(entra: Boolean) {
            try {
                val mp: MediaPlayer = if (entra){
                    MediaPlayer.create(context, R.raw.correct)
                } else {
                    MediaPlayer.create(context, R.raw.error)
                }
                mp.start()
            } catch (e: Exception) {
                Log.d("gta AccessActivity - errorSound","Exception while playing sound:$e")
            }
        }

        fun clearAccessPerson(){
            viewModelScope.launch {
                this@ConsultaViewModel.accessPerson.emit(
                    AccessPerson()
                )
            }
        }

        fun clearMessage(id: Long) {
            viewModelScope.launch {
                uiMessageManager.clearMessage(id)
            }
        }

}