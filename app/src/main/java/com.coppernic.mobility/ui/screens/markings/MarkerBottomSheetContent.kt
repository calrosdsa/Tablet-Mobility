package com.coppernic.mobility.ui.screens.markings

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coppernic.mobility.domain.util.MarcacionEstado
import com.coppernic.mobility.domain.util.TipoDeMarcacion

@Composable
fun MarkerBottomSheetContent(
    selectTipoDeMarcacion:(TipoDeMarcacion)->Unit,
    currentTipoDeMarcacion: TipoDeMarcacion,
    datePickerDialog: DatePickerDialog,
    setMarcacionEstado:(MarcacionEstado)->Unit,
    currentMarcacionEstado: MarcacionEstado,
    setDateCalendar:()->Unit,
    setFilters:()->Unit,
    dateValue:String
) {

    Column(
        modifier = Modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tipo de Accesso",
            modifier = Modifier.padding(start = 5.dp, top = 5.dp, end = 70.dp),
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 15.sp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, start = 5.dp)
        ) {
            Button(
                onClick = { selectTipoDeMarcacion(TipoDeMarcacion.ALL) },
                modifier = Modifier.alpha(if (TipoDeMarcacion.ALL == currentTipoDeMarcacion) 1f else 0.6f)
            ) {
                Text(text = "Todo", color = MaterialTheme.colors.background)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { selectTipoDeMarcacion(TipoDeMarcacion.INGRESO) },
                modifier = Modifier.alpha(if (TipoDeMarcacion.INGRESO == currentTipoDeMarcacion) 1f else 0.6f)
            ) {
                Text(text = "Ingreso", color = MaterialTheme.colors.background)
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = { selectTipoDeMarcacion(TipoDeMarcacion.SALIDA) },
                modifier = Modifier.alpha(if (TipoDeMarcacion.SALIDA == currentTipoDeMarcacion) 1f else 0.6f)
            ) {
                Text(text = "Salida", color = MaterialTheme.colors.background)
            }
        }


        Text(
            text = "Estado de la Marcacion",
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 15.sp),
            modifier = Modifier.padding(start = 5.dp, top = 5.dp, end = 70.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp,)
        ) {
            Button(
                onClick = {
                    setMarcacionEstado(MarcacionEstado.All)
                },
                modifier = Modifier.alpha(if (MarcacionEstado.All == currentMarcacionEstado) 1f else 0.6f)
            ) {
                Text(text = "Todo", color = MaterialTheme.colors.background)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { setMarcacionEstado(MarcacionEstado.ENVIADO) },
                modifier = Modifier.alpha(if (MarcacionEstado.ENVIADO == currentMarcacionEstado) 1f else 0.6f)
            ) {
                Text(text = "Enviados", color = MaterialTheme.colors.background)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { setMarcacionEstado(MarcacionEstado.PENDIENTE) },
                modifier = Modifier.alpha(if (MarcacionEstado.PENDIENTE == currentMarcacionEstado) 1f else 0.6f)
            ) {
                Text(text = "Pendientes", color = MaterialTheme.colors.background)
            }
        }

        Text(
            text = "Filtrar por tiempo",
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 15.sp),
            modifier = Modifier.padding(start = 5.dp, top = 5.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = dateValue, color = MaterialTheme.colors.background)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.EditCalendar, contentDescription = "calendar",
                        tint = Color.White
                    )
                }
            }
            if (dateValue.isNotBlank()) {
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = { setDateCalendar() }) {
                    Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "Cancel Date")
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colors.primary)
            .height(45.dp)
            .clickable {
                setFilters()
            }
        ) {
            Text(
                text = "ENVIAR",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.subtitle2.copy(fontSize = 15.sp),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
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