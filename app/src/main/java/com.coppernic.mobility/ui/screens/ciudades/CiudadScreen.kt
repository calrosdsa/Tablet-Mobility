package com.coppernic.mobility.ui.screens.ciudades

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coppernic.mobility.ui.components.TopBarComponent
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.util.constants.MainDestination
import kotlinx.coroutines.launch

@Composable
fun CiudadesScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: CiudadesVIiewModel = hiltViewModel()
) {
    val coroutine = rememberCoroutineScope()
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)

    state.message?.let {message->
        LaunchedEffect(key1 = message, block = {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage(message.id)
        })
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {padding->

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        if(state.ciudades.isNotEmpty()){

        TopBarComponent(openMenu = {
            coroutine.launch {
                scaffoldState.drawerState.open()
            }
        }) { navController.popBackStack() }

        Divider(modifier = Modifier.padding(top = 50.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            state.ciudades.map {
                TextButton(onClick = { navController.navigate(MainDestination.MUSTERING_ROUTE + "/${it.id}") }) {
                    Text(text = it.nombre)
                }
            }
        }
        }else{

//        if (state.value.isEmpty()) {
            Text(
                text = "No hay ciudades disponibles",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
//        }
        }
    }
}
}
