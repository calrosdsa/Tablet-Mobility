package com.coppernic.mobility.tasks

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
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


class SendRioWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("RIO_WORKER","BEGIN")
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val db = AppDatabase.getDatabase(applicationContext)
        Log.d("RIO_WORKER","BEGIN2")
        //obtener data config
        val configuraciones = db.configDao().getConfig()
        Log.d("RIO_WORKER","BEGIN3")
        Log.d("RIO_WORKER",configuraciones.toString())
        if(configuraciones.isEmpty()){
            Log.d("RIO_WORKER","RETURN")
            return Result.success()
        }
        Log.d("RIO_WORKER","BEGIN2")
        val config = configuraciones[0]
        //obtener data marcaciones
        val marcaciones = db.marcacionDao().getMarcacionesPendientes()
        Log.d("SendRioWorker",marcaciones.size.toString())

        // INTENTAR ENVIAR CON RIO
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
            Log.d("SendRioWorker", "https://${config.url_controladora}/Login")
            var response: HttpResponse = client.submitForm(
                url = "https://${config.url_controladora}/Login",
                formParameters = Parameters.build {
                    append("username", config.riouser!!)
                    append("password", config.riopass!!)
                },
                encodeInQuery = false
            )
            Log.d("SendRioWorker", response.receive() as String)
            if (response.receive() as String == "Login successful") {
                Log.d("SendRioWorker", "${marcaciones.size} marcaciones pendientes")
                //enviar encendido de puerta
                var u1 = "https://${config.url_controladora}/ExternalIntegrations/${config.interfaz}/Update"
                Log.d("SendRioWorker", u1)
                response = client.post("https://${config.url_controladora}/ExternalIntegrations/${config.interfaz}/Update") {
                    contentType(ContentType.Application.Xml)
                    body = "<Request>\n" +
                            "   <BusUpdate>\n" +
                            "     <SetConnected>\n" +
                            "       <Interface>${config.interfaz}</Interface>\n" +
                            "       <IsConnected>True</IsConnected>\n" +
                            "       <SpecificDevices>\n" +
                            "         <None />\n" +
                            "       </SpecificDevices>\n" +
                            "     </SetConnected>\n" +
                            "   </BusUpdate>\n" +
                            "</Request>"
                }
                Log.d("SendRioWorker", response.toString())
                if (response.receive() as String == "<Response>OK</Response>") {
                    response = client.post("https://${config.url_controladora}/ExternalIntegrations/${config.interfaz}/Update") {
                        contentType(ContentType.Application.Xml)
                        body = "<Request>\n" +
                                "   <BusUpdate>\n" +
                                "     <StatusKeepalive>\n" +
                                "       <Duration>00:01:00</Duration>\n" +
                                "     </StatusKeepalive>\n" +
                                "   </BusUpdate>\n" +
                                "</Request>"
                    }
                    if (response.receive() as String == "<Response>OK</Response>") {
                        Log.d("SendRioWorker", "envio de offline programado exito")
                    } else {
                        Log.d("SendRioWorker", "envio de offline programado fallo")
                    }
                } else {
                    Log.d("SendRioWorker", "envio de online fallo")
                }

//                val marcacion = marcaciones[0]
                //enviar marcaciones
                for ( marcacion in marcaciones){
                    Log.d("SendRioWorker", "tarjeta ${marcacion.cardCode} enviando")
//                val zone: ZoneId = ZoneId.of("GMT-4")
//                val date = LocalDateTime.ofEpochSecond(marcacion.fecha,0, ZoneOffset.UTC)
//                Log.d("SendRioWorker", "time $date")
//                val zoneOffset = zone.rules.getOffset(date)
//                Log.d("SendRioWorker", "time $zone")
//                val offsetTime =  OffsetDateTime.of(marcacion.fecha.let { LocalDateTime.ofEpochSecond(it,0 , zoneOffset) }, zoneOffset)
//                Log.d("SendRioWorker", "time $offsetTime")
//                val timestamp = Timestamp.valueOf(offsetTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime().toString())
//                Log.d("SendRioWorker", "timeStamp $timestamp ")
//                    "      <Timestamp>${timestamp}-04:00</Timestamp>\n" +
                            response = client.post("https://${config.url_controladora}/ExternalIntegrations/${config.interfaz}/Update") {
                        contentType(ContentType.Application.Xml)
                        body = "<Request>\n" +
                                "  <BusUpdate>\n" +
                                "    <OfflineDecision>\n" +
                                "      <Interface>${config.interfaz}</Interface>\n" +
                                "      <Reader>${marcacion.tipoMarcacion}</Reader>\n" +
                                "      <Timestamp>${marcacion.date?.toLocalDateTime()}-04:00</Timestamp>\n" +
                                "      <Card>\n" +
                                "        <Some>\n" +
                                "          <BitCount>26</BitCount>\n" +
                                "          <CardCode>${marcacion.cardCode}</CardCode>\n" +
                                "        </Some>\n" +
                                "      </Card>\n" +
                                "      <Granted>${marcacion.acceso}</Granted>\n" +
                                "    </OfflineDecision>\n" +
                                "  </BusUpdate>\n" +
                                "</Request>"
                    }
                    Log.d("SendRioWorker",response.receive<String>().toString())
                    if (response.receive() as String == "<Response>OK</Response>") {
                        //update state in db
                        Log.d("SendRioWorker", "tarjeta ${marcacion.cardCode} enviado con exito")
                        db.marcacionDao().updateMarcacion(marcacion.copy(
                            estado = "enviado",
                        ))
                    } else {
                        Log.d("SendRioWorker", "envio de marcacion fallo - ${marcacion.cardCode},${marcacion.fecha}")
                    }
                }
            } else {
                Log.d("SendRioWorker", "login fallo")
            }
        } catch (e: Exception){
            Log.d("SendRioWorker", "coneccion fallo")
            return Result.failure()
            //Log.d("SendRioWorker", if (e.localizedMessage == null) "error conexion" else e.localizedMessage)
        }
        return Result.success()
    }

}