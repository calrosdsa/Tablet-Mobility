package com.coppernic.mobility.domain.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

@get:RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
val Context.networkStatus: Flow<NetworkStatus>
    get() = getNetworkStatusLollipop(this)

@RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
private fun getNetworkStatusLollipop(context: Context): Flow<NetworkStatus> = callbackFlow {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = object : ConnectivityManager.NetworkCallback() {
        private var availabilityCheckJob: Job? = null

        override fun onUnavailable() {
            availabilityCheckJob?.cancel()
            trySend(NetworkStatus.Unavailable)
        }

        override fun onAvailable(network: Network) {
            availabilityCheckJob = launch {
                send(if(checkAvailability()) NetworkStatus.Available else NetworkStatus.Unavailable)
                Toast.makeText(context, "Conectado al Servidor", Toast.LENGTH_SHORT).show()

            }
        }

        override fun onLost(network: Network) {
            availabilityCheckJob?.cancel()
            trySend(NetworkStatus.Unavailable)
        }
    }

    val request = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager.registerNetworkCallback(request, callback)

    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
}

//@RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
//private fun getNetworkStatusPreLollipop(context: Context): Flow<NetworkStatus> = callbackFlow {
//    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    val receiver = object: BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            launch {
//                if (connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true) {
//                    send(if(checkAvailability()) NetworkStatus.Available else NetworkStatus.Unavailable)
//                } else {
//                    send(NetworkStatus.Unavailable)
//                }
//            }
//        }
//    }
//
//    context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//
//    awaitClose { context.unregisterReceiver(receiver) }
//}

private suspend fun checkAvailability() : Boolean = withContext(Dispatchers.IO) {
    try {
        Socket().use {
            it.connect(InetSocketAddress("8.8.8.8", 53))
        }
        true
    } catch (e: Exception){
        e.printStackTrace()
        false
    }
}

enum class NetworkStatus {
    Unavailable,
    Available
}
