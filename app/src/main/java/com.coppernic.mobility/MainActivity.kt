package com.coppernic.mobility

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.coppernic.mobility.domain.util.AppDateFormatter
import com.coppernic.mobility.rfid.Hid
import com.coppernic.mobility.rfid.RfidListener
import com.coppernic.mobility.ui.Home
import com.coppernic.mobility.ui.LocalAppDateFormatter
import com.coppernic.mobility.ui.theme.TecluMobilityTheme
import com.coppernic.mobility.util.interfaces.AppPreferences
import com.coppernic.mobility.util.interfaces.AppTasks
import dagger.hilt.android.AndroidEntryPoint
import fr.coppernic.sdk.hid.iclassProx.Card
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject internal lateinit var appPreferences: AppPreferences
    @Inject internal lateinit var appDateFormatter: AppDateFormatter
    @Inject internal lateinit var appTasks: AppTasks
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
        val passwordPref = appPreferences.passwordStream.collectAsState()
            CompositionLocalProvider(
                LocalAppDateFormatter provides appDateFormatter
            ) {
            TecluMobilityTheme() {
                Home(password = passwordPref.value)
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
        appTasks.sendMarcaciones()
    }
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        }
    override fun onDestroy() {
        super.onDestroy()
        appTasks.mainTask()
    }


}

const val TAG_D = "DEBUG_LOG"