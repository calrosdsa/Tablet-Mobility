package com.coppernic.mobility.domain.useCases

import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.util.Resource
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetConnection @Inject constructor(
    private val apiService: ApiService
){
    operator fun invoke () = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getAccessInicio()
            emit(Resource.Success(response))
        }catch(e: IOException){
            emit(Resource.Error(e.localizedMessage?:"Error inesperado"))
        }
    }
}