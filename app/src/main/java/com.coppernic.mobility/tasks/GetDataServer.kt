package com.coppernic.mobility.tasks

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coppernic.mobility.data.models.dao.ConfigDao
import com.coppernic.mobility.data.models.entities.Config
import com.coppernic.mobility.domain.interactors.UpdateCardHolders
import com.coppernic.mobility.domain.interactors.UpdateCredentials
import com.coppernic.mobility.domain.useCases.GetSettings
import com.coppernic.mobility.domain.util.ObservableLoadingCounter
import com.coppernic.mobility.domain.util.UiMessageManager
import com.coppernic.mobility.domain.util.collectStatus
import com.coppernic.mobility.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


@HiltWorker
class GetDataServer @AssistedInject constructor(
    @Assisted context:Context,
    @Assisted params: WorkerParameters,
//    private val getSettings: GetSettings,
//    private val configDao:ConfigDao,
    private val updateCredentials: UpdateCredentials,
    private val updateCardHolders: UpdateCardHolders,
) :CoroutineWorker(context,params) {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    companion object{
        const val TAG = "get_data_server"
    }

    override suspend fun doWork(): Result {
//        val telephoneManger =  applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return try{
            runBlocking {
            val cardHolders = async {
                updateCardHolders.executeSync(UpdateCardHolders.Params(true))
            }
            val credentials = async {
                updateCredentials.executeSync(UpdateCredentials.Params(true))
            }
            cardHolders.await()
            credentials.await()
            }
//                    updateCardHolders.executeSync(UpdateCardHolders.Params(true))
//                    updateCredentials.executeSync(UpdateCredentials.Params(true))
            Result.success()
        }catch(e:Exception){
            Result.failure()
        }
//        getSettings().collect{result->
//            when(result){
//                is Resource.Success -> {
//                    result.data?.data?.let {
//                    configDao.insertConfig(
//                        Config(
//                            id=it.id,
//                            coordenada = it.coordenadas,
//                            interfaz = it.interfas,
//                            riopass = it.passwordRio,
//                            riouser = it.usuarioRio,
//                            zonaHoraria = it.ZonaHoraria,
//                            url_controladora = it.ipControlador,
//                            url_servidor = "http://172.20.10.55:91",
//                            localePass ="129192",
//                            zonaPoligono = "Zona Poligono",
//                            imei = telephoneManger.imei,
//                            ciudades = it.ciudades
//                        )
//                    )
//                    }
//                    Log.d("CONGIG_RESULT", "SUCCESS")
//                }
//                is Resource.Error ->{
//                    Toast.makeText(applicationContext,result.message,Toast.LENGTH_SHORT).show()
//                    Result.retry()
//                }
//                else -> {}
//            }
//        }
    }
}