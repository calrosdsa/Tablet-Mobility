package com.coppernic.mobility.ui.screens.markings

import com.coppernic.mobility.domain.util.MarcacionEstado
import com.coppernic.mobility.domain.util.TipoDeMarcacion

data class MarkedState(
//    val markers:List<MarcacionWithImage> = emptyList(),
    val sortedOptions:Boolean= false ,
    val tipoDeMarcacion: TipoDeMarcacion = TipoDeMarcacion.ALL,
    val marcacionState:MarcacionEstado  = MarcacionEstado.All,

)

