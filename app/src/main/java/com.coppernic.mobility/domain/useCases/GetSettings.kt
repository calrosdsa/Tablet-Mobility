package com.coppernic.mobility.domain.useCases

import android.Manifest
import android.content.Context
import android.telephony.TelephonyManager
import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.data.dto.settings.SettingsDto
import com.coppernic.mobility.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
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
//        val requiredPermission = Manifest.permission.READ_CONTACTS
//        val checkVal: Int = context.checkCallingOrSelfPermission(requiredPermission)
            try{
                val telephoneManger =  context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//                Log.d("SETTING_REQUEST","Begin")
                emit(Resource.Loading())
                val response = apiService.getSettings(telephoneManger.imei)
//                val response = apiService.getSettings("353547080165635")
//            Log.d("SETTING_REQUEST","Success ${response")
//                Log.d("SETTING_REQUEST","Success $response")
//                emit(Resource.Success(data = response, message = telephoneManger.imei))
                emit(Resource.Success(data = response, message = telephoneManger.imei))
            }catch(e: HttpException){
//                Log.d("SETTING_REQUEST","Http ${e.localizedMessage}")
                emit(Resource.Error(e.localizedMessage?:"Http errror"))
            }catch(e: IOException){
//                Log.d("SETTING_REQUEST","Io ${e.localizedMessage}")
                emit(Resource.Error(e.localizedMessage?: "IoException"))
            }
    }
}