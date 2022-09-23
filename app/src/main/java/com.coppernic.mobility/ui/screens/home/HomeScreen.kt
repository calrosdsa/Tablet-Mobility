package com.coppernic.mobility.ui.screens.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coppernic.mobility.domain.util.NetworkStatus
import com.coppernic.mobility.ui.components.TopBar
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.util.constants.Params
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.coppernic.mobility.R
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
   navController: NavController,
   scaffoldState: ScaffoldState,
   viewModel: HomeViewModel = hiltViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val context = LocalContext.current as Activity
    val openDialog = remember {
        mutableStateOf(false)
    }
    val nameTablet = viewModel.tableName.collectAsState().value
//    val marcacionValidate by remember(state.marcacionCount) {
//        derivedStateOf {
//            if(state.marcacionCount == 1) 0 else state.marcacionCount
//        }
//    }

    LaunchedEffect(key1 = Unit, key2 = state.loading, block = {
        viewModel.getMarcarcaionCount()
    })

    state.message?.let { message ->
        LaunchedEffect(message) {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage(message.id)
        }
    }
    BackHandler {
        context.finishAffinity()
    }
    LaunchedEffect(key1 = viewModel, block = {
        Log.d("ACCESS_DEBUG","RENDER")
    })

    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colors.background)
                    .padding(40.dp)
            ) {
                Text(text = "Panel Name", style = MaterialTheme.typography.subtitle2)
                Text(text = nameTablet, style = MaterialTheme.typography.subtitle1)
            }
        }
    }

    Column() {
        TopBar(
            openMenu = {
                coroutine.launch {
                    scaffoldState.drawerState.open()
                }
            },
            onRefresh = {
                viewModel.updateCardHolders()
            },
            onOpenDialog = { openDialog.value = true },
            marcacionesCount = state.marcacionCount,
            navigateTo = { navController.navigate(MainDestination.MARKINGS_ROUTE){
                launchSingleTop = true
            } },
            tableName= nameTablet
        )
        Divider()
        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = state.loading),
            onRefresh = { viewModel.updateCardHolders() },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    scale = true
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "main_logo",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .height(140.dp)
                )
//                    Spacer(modifier = Modifier.height(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate(MainDestination.WAITING_SCREEN + "?${Params.TYPE_ACCESS_PARAM}=R1") {
                                launchSingleTop = true
                            }
                        }, modifier = Modifier
                            .height(60.dp)
                            .width(120.dp)
                    ) {
                        Text(
                            text = "Ingreso",
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.background
                        )
                    }
                    Button(
                        onClick = {
                            navController.navigate(MainDestination.WAITING_SCREEN + "?${Params.TYPE_ACCESS_PARAM}=R2") {
                                launchSingleTop = true
                            }
                        }, modifier = Modifier
                            .height(60.dp)
                            .width(120.dp)
                    ) {
                        Text(
                            text = "Salida",
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.background
                        )
                    }
                }
                Text(
                    text = if (state.netWorkConnection == NetworkStatus.Unavailable) "Sin Conexion" else "Conectado al Servidor",
                    style = MaterialTheme.typography.h6, modifier = Modifier
                        .padding(20.dp)
                )
            }
        }
    }
}
//}
//    if(deleteAlert){
//    Dialog(onDismissRequest = { deleteAlert = false }) {
//        Column(modifier = Modifier
//            .fillMaxWidth(0.82f)
//            .height(150.dp)
//            .padding(10.dp)) {
//            Text(text = "Ingresa la contraseña")
//            TextField(value = password, onValueChange = {
//                password = it
//            })
//
//            Row(modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.End) {
//                TextButton(onClick = { deleteAlert = false }) {
//                    Text(text = "CANCELAR",color = MaterialTheme.colors.primary)
//                }
//                TextButton(onClick = {
//                    if(state.passwordPref == password){
//
//                    }
//                }) {
//                    Text(text = "INGRESAR",color = MaterialTheme.colors.primary)
//                }
//            }
//
//        }
//    }
//    }
