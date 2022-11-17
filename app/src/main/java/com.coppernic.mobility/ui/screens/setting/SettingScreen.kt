package com.coppernic.mobility.ui.screens.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coppernic.mobility.ui.components.DialogConfirmation
import com.coppernic.mobility.ui.components.TransparentTextField
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.ui.screens.markings.RadioButtonOption
import com.coppernic.mobility.util.constants.MainDestination
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: SettingViewModel = hiltViewModel()
){
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val route = viewModel.route_stream.collectAsState().value
    val pinValue = viewModel.pin.collectAsState().value
    val accessPinValue = viewModel.accessPin.collectAsState().value
    val coroutine = rememberCoroutineScope()
    val password = remember(pinValue) {
        mutableStateOf(pinValue)
    }
    val accessPin = remember(accessPinValue) {
        mutableStateOf(accessPinValue)
    }
    val urlServidor = remember(state.url_servidor) {
        mutableStateOf(state.url_servidor)
    }
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    var pinVisibility by remember {
        mutableStateOf(false)
    }
    //Para cambiar la pantalla de inicio
    var routeStatus by remember {
        mutableStateOf(route == MainDestination.INITIAL_SCREEN)
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
        },
    modifier = Modifier.fillMaxSize()
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

                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Habilitar inicio con verificacion", modifier = Modifier.fillMaxWidth(0.7f), maxLines = 1)
                    Checkbox(checked = routeStatus, onCheckedChange ={
                        routeStatus = !routeStatus
                    } )
                }
                state.settingState?.let {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "IMEI",
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = it.imei,
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f)
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Direccion de Controlador",
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = it.url_controladora.toString(),
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f)
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Interfaz",
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = it.interfaz.toString(),
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f)
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

//                Spacer(modifier = Modifier.height(15.dp))
                }
                    Text(
                        text = "Pin",
                        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.offset(y = 15.dp)
                    )
                TransparentTextField(
                    textFieldValue = password,
                    textLabel = "",
                    keyboardType = KeyboardType.Password,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focus.clearFocus()
                        }
                    ),
                    visualTransformation = if(passwordVisibility) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisibility = !passwordVisibility
                            }
                        ) {
                            Icon(
                                imageVector = if(passwordVisibility) {
                                    Icons.Default.Visibility
                                } else {
                                    Icons.Default.VisibilityOff
                                },
                                tint = MaterialTheme.colors.primary,
                                contentDescription = "Toggle Password Icon"
                            )
                        }
                    },
                    imeAction = ImeAction.Done
                )
                Text(
                    text = "Pin de Acceso",
                    color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.offset(y = 15.dp)
                )
                TransparentTextField(
                    textFieldValue = accessPin,
                    textLabel = "",
                    keyboardType = KeyboardType.Password,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focus.clearFocus()
                        }
                    ),
                    visualTransformation = if(pinVisibility) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                pinVisibility = !pinVisibility
                            }
                        ) {
                            Icon(
                                imageVector = if(pinVisibility) {
                                    Icons.Default.Visibility
                                } else {
                                    Icons.Default.VisibilityOff
                                },
                                tint = MaterialTheme.colors.primary,
                                contentDescription = "Toggle Password Icon"
                            )
                        }
                    },
                    imeAction = ImeAction.Done
                )

                Text(
                    text = "Direccion del Servidor:",
                    color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.offset(y = 15.dp)
                )
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
                    if(routeStatus) viewModel.setInitialScreen(MainDestination.INITIAL_SCREEN) else {
                        viewModel.setInitialScreen(MainDestination.HOME_ROUTE)
                    }
                    if(password.value.isNotBlank()) viewModel.setPassword(password.value) else {
                        scaffoldState.snackbarHostState.showSnackbar("Pin no puede estar vacío")
                         return@launch
                    }
                    if(accessPin.value.isNotBlank()) viewModel.setPinAccess(accessPin.value)else {
                        scaffoldState.snackbarHostState.showSnackbar("Access pin no puede estar vacío")
                        return@launch
                    }
                    passwordVisibility = false
                    pinVisibility = false
                    focus.clearFocus()
                    scaffoldState.snackbarHostState.showSnackbar("Guardado  correctamente")
                }
            }
        )
    }
}
