package com.coppernic.mobility.domain.useCases

import com.coppernic.mobility.data.ApiService
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
    operator fun invoke(idEstado: Int, idCiudad: Int): Flow<Resource<MusteringByZonaDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getEstadoPerson(idEstado, idCiudad)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Http errror"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "IoException"))
        }
    }
}