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


@HiltWorker
class GetDataServer @AssistedInject constructor(
    @Assisted context:Context,
    @Assisted params: WorkerParameters,
    private val getSettings: GetSettings,
    private val configDao:ConfigDao,
    private val updateCredentials: UpdateCredentials,
    private val updateCardHolders: UpdateCardHolders,
) :CoroutineWorker(context,params) {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    companion object{
        const val TAG = "get_data_server"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val telephoneManger =  applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        updateCardHolders(UpdateCardHolders.Params(true)).collectStatus(loadingCounter,uiMessageManager)
        updateCredentials(UpdateCredentials.Params(true)).collectStatus(loadingCounter,uiMessageManager)
        getSettings().collect{result->
            when(result){
                is Resource.Success -> {
                    result.data?.data?.let {
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
                    Log.d("CONGIG_RESULT", "SUCCESS")
                }
                is Resource.Error ->{
                    Toast.makeText(applicationContext,result.message,Toast.LENGTH_SHORT).show()
                    Result.retry()
                }
                else -> {}
            }
        }
        return Result.success()
    }
}