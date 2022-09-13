package com.coppernic.mobility.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.coppernic.mobility.domain.util.AppDateFormatter

val LocalAppDateFormatter = staticCompositionLocalOf<AppDateFormatter> {
    error("DateFormatter not provided")
}

//val LocalTiviTextCreator = staticCompositionLocalOf<TiviTextCreator> {
  //  error("TiviTextCreator not provided")
//}
