package com.coppernic.mobility.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coppernic.mobility.ui.components.DialogConfirmation
import com.coppernic.mobility.ui.components.TransparentTextField
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: SettingViewModel = hiltViewModel()
){
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val coroutine = rememberCoroutineScope()
    val password = remember {
        mutableStateOf("")
    }
    val urlServidor = remember(state.url_servidor) {
        mutableStateOf(state.url_servidor)
    }
    val alertDialog = remember { mutableStateOf(false) }
    val focus = LocalFocusManager.current

    state.message?.let { message->
        LaunchedEffect(key1 = message, block = {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage(message.id)
        })
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "arrow_back_setting"
                    )
                }
                TextButton(onClick = {
                    alertDialog.value = true
                }) {
                    Text(text = "Guardar")
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.executeAsync() }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription ="resincronizar" )
            }
        }
    ) { padding ->


        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colors.background)
        ) {
            if(state.loading){
                Dialog(onDismissRequest ={}) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            Column(modifier = Modifier.padding(15.dp)) {
                state.settingState?.let {

                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "IMEI:",
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = it.imei,
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Direccion de Controlador",
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = it.url_controladora.toString(),
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Direccion del Servidor:",
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.offset(y = 15.dp)
                    )
//                Spacer(modifier = Modifier.height(15.dp))
                    TransparentTextField(
                        textFieldValue = urlServidor,
                        textLabel = "",
                        keyboardType = KeyboardType.Text,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focus.clearFocus()
                            }
                        ),
                        imeAction = ImeAction.Done
                    )


                    Spacer(modifier = Modifier.height(15.dp))
                    TransparentTextField(
                        textFieldValue = password,
                        textLabel = "Nueva Contraseña",
                        keyboardType = KeyboardType.Text,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focus.clearFocus()
                            }
                        ),
                        imeAction = ImeAction.Done
                    )
                }
            }
        }
    }

    if(alertDialog.value){
        DialogConfirmation(
            onDismiss = { alertDialog.value = false },
            onAction = {
                coroutine.launch {
                    alertDialog.value = false
                    viewModel.setUrlServidor(urlServidor.value)
                    if(password.value.isNotBlank()) viewModel.setPassword(password.value)
                    password.value = ""
                    scaffoldState.snackbarHostState.showSnackbar("Guardado  correctamente")
                    focus.clearFocus()
                }
            }
        )
    }
}
