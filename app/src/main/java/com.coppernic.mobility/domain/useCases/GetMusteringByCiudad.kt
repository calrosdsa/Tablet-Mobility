package com.coppernic.mobility.domain.useCases

import android.util.Log
import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.data.dto.mustering.MusteringByCiudadDto
import com.coppernic.mobility.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMusteringByCiudad @Inject constructor(
    private val apiService: ApiService
) {
    operator fun invoke(ciudadId:Int) : Flow<Resource<MusteringByCiudadDto>> = flow {
        try {
            Log.d("MUSTERING","new_request")
            emit(Resource.Loading())
            val response = apiService.getMusteringByCiudad(ciudadId)
            Log.d("MUSTERING",response.data.toString())
            emit(Resource.Success(response))
        }catch(e: HttpException){
//                Log.d("SETTING_REQUEST","Http ${e.localizedMessage}")
            Log.d("Mustering", e.localizedMessage?:"Error Http")
            emit(Resource.Error(e.localizedMessage?:"Http errror"))
        }catch(e: IOException){
            Log.d("Mustering", e.localizedMessage?:"ErrorException")
//                Log.d("SETTING_REQUEST","Io ${e.localizedMessage}")
            emit(Resource.Error(e.localizedMessage?: "IoException"))
        }
    }
}