package com.coppernic.mobility.data

import com.coppernic.mobility.data.dto.access.AccessDataDto
import com.coppernic.mobility.data.dto.cardHolder.CardHolder
import com.coppernic.mobility.data.dto.credentials.Credentials
import com.coppernic.mobility.data.dto.mustering.MusteringByCiudadDto
import com.coppernic.mobility.data.dto.mustering.MusteringByZonaDto
import com.coppernic.mobility.data.dto.settings.SettingsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("CardHolder/cardlist")
    suspend fun getCardHolders(): CardHolder

    @GET("Settings/accessSettingsList")
    suspend fun getAccessSettings():AccessDataDto

    @GET("Settings/accessInicioList")
    suspend fun getAccessInicio():AccessDataDto
    @GET("Credential/credentiallist")
    suspend fun getCredentials(): Credentials

    @GET("Settings/list")
    suspend fun getSettings(
//        @Path("imei") imei:String,
        @Query("imei")  imei:String,
        ):SettingsDto

    @GET("mustering/marcaciones")
    suspend fun getMusteringByCiudad
                (@Query("idciudad") idciudad:Int):MusteringByCiudadDto

    @GET("mustering/marcacionesZona")
    suspend fun getMusteringByZona(
        @Query("idZona") idZona:Int,
        @Query("idCiudad") idCiudad:Int
    ):MusteringByZonaDto

    @GET("Mustering/marcacionesEstado")
    suspend fun getEstadoPerson(
        @Query("idEstado") idEstado:Int,
        @Query("idCiudad") idCiudad:Int
    ):MusteringByZonaDto
//    companion object {
//        val instance by lazy {
//            Retrofit.Builder()
//                .baseUrl("http://172.20.10.55:91/api/")
//                //    .client(OkHttpClient.Builder()
//                //      .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build())
//                .build()
//                .create(ApiService::class.java)
//        }
//    }
}