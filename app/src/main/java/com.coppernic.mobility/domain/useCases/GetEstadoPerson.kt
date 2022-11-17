package com.coppernic.mobility.domain.useCases

import android.util.Log
import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.data.dto.mustering.DataX
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetEstadoPerson @Inject constructor(
    private val apiService: ApiService
) {
    operator fun invoke(
        idEstado: Int, idCiudad: Int,
        query:String
    ): Flow<Resource<List<DataX>>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getEstadoPerson(idEstado, idCiudad)
//            Log.d("RESPONSE_D",response.toString())
            if(query.isBlank()){
            emit(Resource.Success(response.data))
            }else{
//                Log.d("DEBUG_D",query)
            val filterData = response.data.filter { it.nombre.lowercase().contains(query.lowercase()) }
                emit(Resource.Success(filterData))

            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Http errror"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "IoException"))
        }
    }
}