package com.coppernic.mobility

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.coppernic.mobility.domain.util.AppDateFormatter
import com.coppernic.mobility.ui.Home
import com.coppernic.mobility.ui.LocalAppDateFormatter
import com.coppernic.mobility.ui.theme.TecluMobilityTheme
import com.coppernic.mobility.util.interfaces.AppPreferences
import com.coppernic.mobility.util.interfaces.AppTasks
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject internal lateinit var appPreferences: AppPreferences
    @Inject internal lateinit var appDateFormatter: AppDateFormatter
    @Inject internal lateinit var appTasks: AppTasks
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appTasks.getDataServer()
        installSplashScreen()
        setContent {
            val initialRoute = appPreferences.initialScreen

            CompositionLocalProvider(
                LocalAppDateFormatter provides appDateFormatter
            ) {
            TecluMobilityTheme() {
                Home(initialRoute)
            }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { _ ->

        }
        locationPermissionRequest.launch(arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,xx
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE))
//        appTasks.sendMarcaciones()
    }

    override fun onStop() {
        super.onStop()
        appTasks.getDataServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        appTasks.mainTask()
    }


}



const val TAG_D = "DEBUG_LOG"