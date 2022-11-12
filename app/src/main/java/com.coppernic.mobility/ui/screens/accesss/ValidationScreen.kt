package com.coppernic.mobility.ui.screens.accesss

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.coppernic.mobility.R
import com.coppernic.mobility.ui.components.TransparentTextField
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.util.constants.MainDestination
import kotlinx.coroutines.launch

@Composable
fun ValidationScreen(
    navController: NavController,
    scaffoldState:ScaffoldState,
    viewModel: InitialViewModel = hiltViewModel(),
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val focusRequester = remember { FocusRequester() }
    val coroutine = rememberCoroutineScope()
    var deleteAlert by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val password = remember {
        mutableStateOf("")
    }
    var passwordVisibility by remember { mutableStateOf(false) }
    val onError  =  remember { mutableStateOf(false) }

    state.message?.let { message ->
        LaunchedEffect(key1 = message, block = {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage(message.id)
        })
    }
    val valueText = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = valueText.value, block = {
        if (valueText.value.length == 30) {
            viewModel.getCode(valueText.value, valueText)
//            valueText.value = ""
        }
    })

    LaunchedEffect(key1 = state.isAuthenticated, block = {
        if(state.isAuthenticated){
            navController.navigate(MainDestination.HOME_ROUTE)
        }
    })
//    state.binaryCode?.let {
//    LaunchedEffect(key1 = it, block = {
//        valueText.value = ""
//    })
//    }


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_encode))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    CompositionLocalProvider(
        LocalTextInputService provides null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TextField(
                value = valueText.value, onValueChange = {
                    valueText.value = it
                }, modifier = Modifier
                    .focusRequester(focusRequester)
                    .alpha(0f)
                    .size(0.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
             Spacer(modifier = Modifier.height(1.dp))
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.testTag("logo_animation")
                )
                Text(
                    text = "Ingrese el código o escaneé su tarjeta para continuar",
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.secondaryVariant
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Button(onClick = {
//           viewModel.setAccessPerson()
                    deleteAlert = true
                }
                ) {
                    Text(
                        text = " Ingresar Código",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.background
                    )
                }
            }
        }

        if(deleteAlert){
            Dialog(onDismissRequest = { deleteAlert = false }) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .height(180.dp)
                    .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Ingresa la contraseña",color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f))
//                TextField(value = password, onValueChange = {
//                    password = it
//                }, modifier = Modifier.fillMaxWidth())

                    TransparentTextField(
                        textFieldValue = password,
                        textLabel = "Contraseña",
                        keyboardType = KeyboardType.Password,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                if(viewModel.pin == password.value || viewModel.accessPin == password.value){
                                    onError.value = false
                                    password.value = ""
                                    deleteAlert = false
                                    navController.navigate(MainDestination.CONFIGURATION_ROUTE)
                                    coroutine.launch {
                                        scaffoldState.drawerState.close()
                                    }
                                }else{
                                    onError.value = true
                                }

                            }
                        ),
                        imeAction = ImeAction.Done,
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
                        error = onError.value,
                        visualTransformation = if(passwordVisibility) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                    )

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { deleteAlert = false }) {
                            Text(text = "CANCELAR",color = MaterialTheme.colors.primary)
                        }
                        TextButton(onClick = {
                            if(viewModel.pin == password.value || viewModel.accessPin == password.value){
                                onError.value = false
                                password.value = ""
                                deleteAlert = false
                                navController.navigate(MainDestination.HOME_ROUTE)
                                coroutine.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }else{
                                onError.value = true
                            }
                        }) {
                            Text(text = "INGRESAR",color = MaterialTheme.colors.primary)
                        }
                    }

                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
//        Log.d("REQUEST_","TRUE")
        }
    }
}