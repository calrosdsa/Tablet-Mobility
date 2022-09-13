package com.coppernic.mobility.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coppernic.mobility.data.AppDatabase
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import nl.altindag.ssl.SSLFactory

class SendOnlineWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        //obtener data config
        val configuraciones = db.configDao().getConfig()
        Log.d("RIO_REQUEST",configuraciones.toString())
        if(configuraciones.isEmpty()) return Result.success()
        val config = configuraciones[0]
        val sslFactory = SSLFactory.builder()
            .withTrustingAllCertificatesWithoutValidation()
            .build()
        val client = HttpClient(Android) {
            engine {
                sslManager = { httpsURLConnection ->
                    httpsURLConnection.hostnameVerifier = sslFactory.hostnameVerifier
                    httpsURLConnection.sslSocketFactory = sslFactory.sslSocketFactory
                }
            }
            install(HttpCookies) {
                // Will keep an in-memory map with all the cookies from previous requests.
                storage = AcceptAllCookiesStorage()
            }
        }
        //make login
        try {
            Log.d("SendOnlineWorker", "https://${config.url_controladora}/Login")
            var response: HttpResponse = client.submitForm(
                url = "https://${config.url_controladora}/Login",
                formParameters = Parameters.build {
                    append("username", config.riouser!!)
                    append("password", config.riopass!!)
                },
                encodeInQuery = false
            )
            Log.d("SendOnlineWorker", response.receive() as String)
            if (response.receive() as String == "Login successful") {
                Log.d("SendOnlineWorker", "enviando online")
                response = client.post("https://${config.url_controladora}/ExternalIntegrations/${config.interfaz}/Update") {
                    contentType(ContentType.Application.Xml)
                    body = "<Request>\n" +
                            "   <BusUpdate>\n" +
                            "     <SetConnected>\n" +
                            "       <Interface>Address1</Interface>\n" +
                            "       <IsConnected>True</IsConnected>\n" +
                            "       <SpecificDevices>\n" +
                            "         <None />\n" +
                            "       </SpecificDevices>\n" +
                            "     </SetConnected>\n" +
                            "   </BusUpdate>\n" +
                            "</Request>"
                }
                if (response.receive() as String == "<Response>OK</Response>") {
                    response = client.post("https://${config.url_controladora}/ExternalIntegrations/${config.interfaz}/Update") {
                        contentType(ContentType.Application.Xml)
                        body = "<Request>\n" +
                                "   <BusUpdate>\n" +
                                "     <StatusKeepalive>\n" +
                                "       <Duration>00:02:00</Duration>\n" +
                                "     </StatusKeepalive>\n" +
                                "   </BusUpdate>\n" +
                                "</Request>"
                    }
                    if (response.receive() as String == "<Response>OK</Response>") {
                        Log.d("SendOnlineWorker", "envio de offline programado exito")
                    } else {
                        Log.d("SendOnlineWorker", "envio de offline programado fallo")
                    }
                } else {
                    Log.d("SendOnlineWorker", "envio de online fallo")
                }
            } else {
                Log.d("SendOnlineWorker", "login fallo")
            }
        } catch (e: Exception){
            Log.d("SendOnlineWorker", "coneccion fallo")
        }

        return Result.success()
    }
}