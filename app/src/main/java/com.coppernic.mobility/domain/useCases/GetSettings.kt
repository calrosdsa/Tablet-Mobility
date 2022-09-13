package com.coppernic.mobility.domain.useCases

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.data.dto.settings.SettingsDto
import com.coppernic.mobility.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSettings @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    operator fun invoke() : Flow<Resource<SettingsDto>> = flow<Resource<SettingsDto>> {
            try{
                val telephoneManger =  context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                Log.d("SETTING_REQUEST","Begin")
                delay(1000)
                emit(Resource.Loading())
                val response = apiService.getSettings(telephoneManger.imei)
//            Log.d("SETTING_REQUEST","Success ${response")
                Log.d("SETTING_REQUEST","Success $response")
                emit(Resource.Success(response))
            }catch(e: HttpException){
                Log.d("SETTING_REQUEST","Http ${e.localizedMessage}")
                emit(Resource.Error(e.localizedMessage?:"Http errror"))
            }catch(e: IOException){
                Log.d("SETTING_REQUEST","Io ${e.localizedMessage}")
                emit(Resource.Error(e.localizedMessage?: "IoException"))
            }
    }
}