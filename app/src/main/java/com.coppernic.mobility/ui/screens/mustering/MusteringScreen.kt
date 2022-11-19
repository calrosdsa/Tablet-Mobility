package com.coppernic.mobility.ui.screens.mustering

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coppernic.mobility.ui.components.TopBarComponent
import com.coppernic.mobility.ui.components.VerticalGrid
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.util.constants.MainDestination
import kotlinx.coroutines.launch

@Composable
fun MusteringScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: MusteringViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val activeValue = remember {
        mutableStateOf(-1)
    }
    val coroutine = rememberCoroutineScope()
    state.uiMessage?.let { message ->
        LaunchedEffect(key1 = message, block = {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage()
        })
    }
    state.musteringByCiudad?.data?.let { result ->
        state.totalCount?.let {
            val seguros = remember {
                it.seguros?.let { it1 -> Animatable(it1) }
            }
            val inseguros = remember {
                it.inseguros?.let { it1 -> Animatable(it1) }
            }
            state.totalCount?.seguros?.let {
                LaunchedEffect(key1 = it, block = {
                    seguros?.animateTo(it, tween(1000))
                })
            }
            state.totalCount?.inseguros?.let {
                LaunchedEffect(key1 = it, block = {
                    inseguros?.animateTo(it, tween(1000))
                })
            }
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    TopBarComponent(openMenu = { coroutine.launch { scaffoldState.drawerState.open() } }) {
                        navController.popBackStack()
                    }
                    Divider()
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    activeValue.value = -1
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Mustering", style = MaterialTheme.typography.h5)
                        Divider(
                            Modifier
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                                .height(5.dp)
                                .background(MaterialTheme.colors.primary)
                                .clip(MaterialTheme.shapes.large)
                        )
                        if (seguros != null && inseguros != null) {
                            PieChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .padding(horizontal = 80.dp),
//                                .align(Alignment.CenterStart),
                                progress = listOf(seguros.value, inseguros.value),
                                colors = listOf(Color.Red, Color(0xFF00C853)),
                                isDonut = true,
                                inseguros = result.cantidadInseguros,
                                seguros = result.cantidadSeguros,
                                activeValue = activeValue
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Column(horizontalAlignment = Alignment.Start) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Card(elevation = 5.dp) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(20.dp)
                                            .clickable {
                                                navController.navigate(MainDestination.ESTADO_PERSON + "?estado=0&ciudadId=${viewModel.ciudadId}")
                                            }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Group,
                                            contentDescription = "PersonMustering",
                                            tint = Color(0xFF00C853), modifier = Modifier
                                                .size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column() {
                                            Text(
                                                text = "${result.cantidadSeguros} P.",
                                                style = MaterialTheme.typography.h6.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                            Text(text = "Seguras")
                                        }

                                    }
                                }
                                Card(elevation = 5.dp) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(20.dp)
                                            .clickable {
                                                navController.navigate(MainDestination.ESTADO_PERSON + "?estado=1&ciudadId=${viewModel.ciudadId}")
                                            }
                                    ) {
                                        Icon(
//                                        painter = painterResource(id = R.drawable.person_icon),
                                            imageVector = Icons.Default.Group,
                                            contentDescription = "PersonMustering2",
                                            tint = Color.Red, modifier = Modifier
                                                .size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column() {
                                            Text(
                                                text = "${result.cantidadInseguros} P.",
                                                style = MaterialTheme.typography.h6.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                            Text(text = "Inseguras")
                                        }

                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Zonas", style = MaterialTheme.typography.h5)
                        Divider(
                            Modifier
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                                .height(5.dp)
                                .background(MaterialTheme.colors.primary)
                                .clip(MaterialTheme.shapes.large)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        VerticalGrid(columns = 2) {
                        result.zonas.map {
                            Card(elevation = 5.dp, modifier = Modifier
                                .height(200.dp)
                                .width(200.dp)
                                .padding(10.dp)
                                .clickable {
                                    navController.navigate(MainDestination.MUSTERONG_ZONE + "?zoneId=${it.id}&ciudadId=${viewModel.ciudadId}")
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = it.nombre, style = MaterialTheme.typography.h5,
                                        textAlign = TextAlign.Center
                                    )
                                    Divider(
                                        Modifier
                                            .padding(horizontal = 30.dp, vertical = 10.dp)
                                            .height(5.dp)
                                            .background(MaterialTheme.colors.primary)
                                            .clip(MaterialTheme.shapes.large)
                                    )
                                    Text(
                                        text = it.cantidad.toString(),
                                        style = MaterialTheme.typography.h6,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        }
                    }
                }
                if (result.zonas.isEmpty()) {
                    Text(
                        text = "No hay Datos disponibles",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

            }
        }
    }
}


//
//@Composable
//private fun DrawArc(
//) {
//    var startAngle by remember { mutableStateOf(0f) }
//    var sweepAngle = remember { Animatable(40f) }
//    var useCenter by remember { mutableStateOf(true) }
//
//    val coroutine = rememberCoroutineScope()
//
//    Canvas(modifier = canvasModifier) {
//        val canvasWidth = size.width
//        val canvasHeight = size.height
//
//        drawArc(
//            brush = Brush.sweepGradient(
//                colors = listOf(Color.Green,Color.Cyan),
//                center = Offset(canvasWidth / 2, canvasHeight / 2)
//            ),
//            startAngle,
//            sweepAngle.value,
//            useCenter,
//            topLeft = Offset((canvasWidth - canvasHeight) / 2, 0f),
//            size = Size(canvasHeight, canvasHeight)
//        )
//    }
//
//    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//        Text(text = "StartAngle ${startAngle.roundToInt()}")
//        Slider(
//            value = startAngle,
//            onValueChange = { startAngle = it },
//            valueRange = 0f..360f,
//        )
//
//        Text(text = "SweepAngle ${sweepAngle.value.roundToInt()}")
//        Slider(
//            value = sweepAngle.value,
//            onValueChange = { sweepAngle.value.plus(it) },
//            valueRange = 0f..360f,
//        )
//
//        Button(onClick = { useCenter  = !useCenter }) {
//
//        }
//        Spacer(modifier = Modifier.height(10.dp))
//        Button(onClick = {
//            coroutine.launch {
//            sweepAngle.animateTo(360f, animationSpec = tween(2000))
//            }
//        }) {
//
//        }
//
////        CheckBoxWithTextRippleFullRow(label = "useCenter", useCenter) {
////            useCenter = it
////        }
//    }
//}
//
//
//@Composable
//private fun TutorialContent() {
//
//    Column(
//        modifier = Modifier
//            .background(backgroundColor)
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//    ) {
//
//        Text(
//            "Canvas Basics2",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//            modifier = Modifier.padding(8.dp)
//        )
//
//
//
//        Text(
//            "Draw Arc",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//            modifier = Modifier.padding(8.dp)
//        )
//        DrawArcExample()
//
//        Text(
//            "Draw Image",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//            modifier = Modifier.padding(8.dp)
//        )
//    }
//
//}
//
//@Composable
//private fun DrawArcExample() {
//
//    DrawArc()
//
//    Spacer(modifier = Modifier.height(10.dp))
//    DrawNegativeArc()
//
//    Spacer(modifier = Modifier.height(10.dp))
//    DrawMultipleArcs()
//}
//
///**
// * **startAngle** Starting angle in degrees. 0 represents 3 o'clock
// *
// * **sweepAngle** Size of the arc in degrees that is drawn clockwise relative to **startAngle**
// */
//
///**
// * In this example range of angles is changed from 0/360 degrees to -180/180 interval
// */
//@Composable
//private fun DrawNegativeArc() {
//    var startAngle2 by remember { mutableStateOf(0f) }
//    var sweepAngle2 by remember { mutableStateOf(60f) }
//    var useCenter2 by remember { mutableStateOf(true) }
//
//    Canvas(modifier = canvasModifier) {
//        val canvasWidth = size.width
//        val canvasHeight = size.height
//
//        drawArc(
//            color = Color.Red,
//            startAngle2,
//            sweepAngle2,
//            useCenter2,
//            topLeft = Offset((canvasWidth - canvasHeight) / 2, 0f),
//            size = Size(canvasHeight, canvasHeight)
//        )
//    }
//
//    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//        Text(text = "StartAngle ${startAngle2.roundToInt()}")
//        Slider(
//            value = startAngle2,
//            onValueChange = { startAngle2 = it },
//            valueRange = -180f..180f,
//        )
//
//        Text(text = "SweepAngle ${sweepAngle2.roundToInt()}")
//        Slider(
//            value = sweepAngle2,
//            onValueChange = { sweepAngle2 = it },
//            valueRange = -180f..180f,
//        )
//
////        CheckBoxWithTextRippleFullRow(label = "useCenter", useCenter2) {
////            useCenter2 = it
////        }
//    }
//}
//
//@Composable
//private fun DrawMultipleArcs() {
//    var startAngleBlue by remember { mutableStateOf(0f) }
//    var sweepAngleBlue by remember { mutableStateOf(120f) }
//    // useCenter selections are commented out, you can uncomment to see how it
//    // changes drawing arc with Stroke style
//    var useCenterBlue by remember { mutableStateOf(false) }
//
//    var startAngleRed by remember { mutableStateOf(120f) }
//    var sweepAngleRed by remember { mutableStateOf(120f) }
//    var useCenterRed by remember { mutableStateOf(false) }
//
//    var startAngleGreen by remember { mutableStateOf(240f) }
//    var sweepAngleGreen by remember { mutableStateOf(120f) }
//    var useCenterGreen by remember { mutableStateOf(false) }
//
//    Canvas(modifier = canvasModifier) {
//        val canvasWidth = size.width
//        val canvasHeight = size.height
//        val arcHeight = canvasHeight - 20.dp.toPx()
//        val arcStrokeWidth = 10.dp.toPx()
//
//        drawArc(
//            color = Color.Blue,
//            startAngleBlue,
//            sweepAngleBlue,
//            useCenterBlue,
//            topLeft = Offset(
//                (canvasWidth - canvasHeight) / 2,
//                (canvasHeight - arcHeight) / 2
//            ),
//            size = Size(arcHeight, arcHeight),
//            style = Stroke(
//                arcStrokeWidth
//            )
//        )
//
//        drawArc(
//            color = Color.Red,
//            startAngleRed,
//            sweepAngleRed,
//            useCenterRed,
//            topLeft = Offset(
//                (canvasWidth - canvasHeight) / 2,
//                (canvasHeight - arcHeight) / 2
//            ),
//            size = Size(arcHeight, arcHeight),
//            style = Stroke(arcStrokeWidth)
//        )
//
//        drawArc(
//            color = Color.Green,
//            startAngleGreen,
//            sweepAngleGreen,
//            useCenterGreen,
//            topLeft = Offset(
//                (canvasWidth - canvasHeight) / 2,
//                (canvasHeight - arcHeight) / 2
//            ),
//            size = Size(arcHeight, arcHeight),
//            style = Stroke(arcStrokeWidth)
//        )
//    }
//
//    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//        Text(text = "StartAngle ${startAngleBlue.roundToInt()}", color = Color.Blue)
//        Slider(
//            value = startAngleBlue,
//            onValueChange = { startAngleBlue = it },
//            valueRange = 0f..360f,
//        )
//
//        Text(text = "SweepAngle ${sweepAngleBlue.roundToInt()}", color = Color.Blue)
//        Slider(
//            value = sweepAngleBlue,
//            onValueChange = { sweepAngleBlue = it },
//            valueRange = 0f..360f,
//        )
//
////        CheckBoxWithTextRippled(label = "useCenter", useCenterBlue) {
////            useCenterBlue = it
////        }
//    }
//
//
//    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//        Text(text = "StartAngle ${startAngleRed.roundToInt()}", color = Color.Red)
//        Slider(
//            value = startAngleRed,
//            onValueChange = { startAngleRed = it },
//            valueRange = 0f..360f,
//        )
//
//        Text(text = "SweepAngle ${sweepAngleRed.roundToInt()}", color = Color.Red)
//        Slider(
//            value = sweepAngleRed,
//            onValueChange = { sweepAngleRed = it },
//            valueRange = 0f..360f,
//        )
//
////        CheckBoxWithTextRippled(label = "useCenter", useCenterRed) {
////            useCenterRed = it
////        }
//    }
//
//
//    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//        Text(text = "StartAngle ${startAngleGreen.roundToInt()}", color = Color.Green)
//        Slider(
//            value = startAngleGreen,
//            onValueChange = { startAngleGreen = it },
//            valueRange = 0f..360f,
//        )
//
//        Text(text = "SweepAngle ${sweepAngleGreen.roundToInt()}", color = Color.Green)
//        Slider(
//            value = sweepAngleGreen,
//            onValueChange = { sweepAngleGreen = it },
//            valueRange = 0f..360f,
//        )

//        CheckBoxWithTextRippled(label = "useCenter", useCenterGreen) {
//            useCenterGreen = it
//        }
//    }
//}
