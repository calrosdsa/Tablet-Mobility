package com.coppernic.mobility.ui.screens.markings

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coppernic.mobility.domain.util.MarcacionEstado
import com.coppernic.mobility.domain.util.TipoDeMarcacion

@Composable
fun MarkerBottomSheetContent(
//    onSelectOption:(dayOptions)->Unit,
//    currentDayOption: dayOptions,
    closeSheetBottom:()->Unit,
    selectTipoDeMarcacion:(TipoDeMarcacion)->Unit,
    currentTipoDeMarcacion: TipoDeMarcacion,
    datePickerDialog: DatePickerDialog,
    setMarcacionEstado:(MarcacionEstado)->Unit,
    currentMarcacionEstado: MarcacionEstado,
    setDateCalendar:()->Unit
){
    IconButton(onClick = { closeSheetBottom() }) {
        
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "ArrowIconBottomSheet",
            modifier = Modifier.size(20.dp))
    }
    Column(modifier = Modifier
        .height(400.dp)
        .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Tipo de Accesso", style = MaterialTheme.typography.h6,modifier = Modifier.padding(start = 5.dp))
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 80.dp, start = 5.dp)
        ) {
            Button(
                onClick = { selectTipoDeMarcacion(TipoDeMarcacion.ALL) },
                modifier = Modifier.alpha(if (TipoDeMarcacion.ALL == currentTipoDeMarcacion) 1f else 0.7f)
            ) {
                Text(text = "Todo",color = MaterialTheme.colors.background)
            }
            Button(
                onClick = { selectTipoDeMarcacion(TipoDeMarcacion.INGRESO) },
                modifier = Modifier.alpha(if (TipoDeMarcacion.INGRESO == currentTipoDeMarcacion) 1f else 0.7f)
            ) {
                Text(text = "Ingreso",color = MaterialTheme.colors.background)
            }
            Button(
                onClick = { selectTipoDeMarcacion(TipoDeMarcacion.SALIDA) },
                modifier = Modifier.alpha(if (TipoDeMarcacion.SALIDA == currentTipoDeMarcacion) 1f else 0.7f)
            ) {
                Text(text = "Salida",color = MaterialTheme.colors.background)
            }
        }


        Text(text = "Estado de la Marcacion", style = MaterialTheme.typography.h6,modifier = Modifier.padding(start = 5.dp, top = 5.dp,end = 70.dp))
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp,end = 40.dp)
        ) {
            Button(
                onClick = {
                    setMarcacionEstado(MarcacionEstado.All)
                          },
                modifier = Modifier.alpha(if (MarcacionEstado.All == currentMarcacionEstado) 1f else 0.7f)
            ) {
                Text(text = "Todo",color = MaterialTheme.colors.background)
            }
            Button(
                onClick = { setMarcacionEstado(MarcacionEstado.ENVIADO) },
                modifier = Modifier.alpha(if (MarcacionEstado.ENVIADO == currentMarcacionEstado) 1f else 0.7f)
            ) {
                Text(text = "Enviados",color = MaterialTheme.colors.background)
            }
            Button(
                onClick = { setMarcacionEstado(MarcacionEstado.PENDIENTE) },
                modifier = Modifier.alpha(if (MarcacionEstado.PENDIENTE == currentMarcacionEstado) 1f else 0.7f)
            ) {
                Text(text = "Pendientes",color = MaterialTheme.colors.background)
            }
        }

        Text(text = "Filtrar por tiempo", style = MaterialTheme.typography.h6,modifier = Modifier.padding(start = 5.dp, top = 5.dp))
        Spacer(modifier = Modifier.height(5.dp))
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.NO_OPTION) },
//            title = "Todas las marcaciones", selected = currentDayOption == dayOptions.NO_OPTION
//        )
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.TODAY)
//                setDateCalendar()
//            },
//            title = "Marcaciones de hoy", selected = currentDayOption == dayOptions.TODAY,
//        )
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.YESTERDAY)
//                setDateCalendar()
//            },
//            title = "Marcaciones de ayer", selected = dayOptions.YESTERDAY == currentDayOption
//        )

        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = " Abrir Calendario",color = MaterialTheme.colors.background)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(imageVector = Icons.Default.EditCalendar, contentDescription = "calendar",
                tint = Color.White)
            }
        }
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.DAY3) },
//            title = "Hace dos dias", selected = dayOptions.DAY3 == currentDayOption
//        )
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.DAY4) },
//            title = "Hace tres dias", selected = dayOptions.DAY4 == currentDayOption
//        )
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.DAY5) },
//            title = "Hace cuatro dias", selected = dayOptions.DAY5 == currentDayOption
//        )
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.DAY6) },
//            title = "Hace cinco dias", selected = dayOptions.DAY6 == currentDayOption
//        )
//        RadioButtonOption(
//            onSelect = { onSelectOption(dayOptions.DAY7) },
//            title = "Hace seis dias", selected = dayOptions.DAY7 == currentDayOption
//        )
    }
}

@Composable
fun RadioButtonOption(
    onSelect:()->Unit,
    title:String,
    selected: Boolean
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onSelect()
        }
        .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.body1)
        RadioButton(selected = selected, onClick = {
            onSelect()
        })
    }
}