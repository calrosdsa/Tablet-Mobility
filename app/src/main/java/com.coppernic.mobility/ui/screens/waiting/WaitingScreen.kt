package com.coppernic.mobility.ui.screens.waiting

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.coppernic.mobility.ui.DisposableEffectWithLifeCycle
import com.coppernic.mobility.ui.components.VerticalGrid
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.R

@Composable
fun WaitingScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: WaitingViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val focusRequester = remember { FocusRequester() }
    state.message?.let { message ->
        LaunchedEffect(key1 = message, block = {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage(message.id)

        })
    }
    LaunchedEffect(key1 = Unit, block = {
        focusRequester.requestFocus()
    })
    var valueText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = valueText, block = {
        if(valueText.length == 30){
            viewModel.getCode(valueText)
            valueText = ""

        }
    })

    BackHandler {
        if (state.personAccess?.accessState != null) {
            viewModel.clearAccessPerson()
        } else {
            navController.popBackStack()
        }
    }
    val userChoice = when (state.userChoice) {
        "R1" -> "Ingreso"
        "R2" -> "Salida"
        else -> ""
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_encode))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    CompositionLocalProvider(
        LocalTextInputService provides null
    ) {
        Scaffold() { padding ->
        TextField(value = valueText, onValueChange = {
            valueText = it
        },modifier = Modifier
            .focusRequester(focusRequester)
            .alpha(0f)
        )
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(
                        state.personAccess?.stateBackGround ?: MaterialTheme.colors.background
                    )
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "icon_back_")
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//           Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//        Button(onClick = { viewModel.checkValidation(213,2937) }) {
//        Text(text = "Permitido")
//        }
//           Button(onClick = { viewModel.checkValidation(123,1137) }) {
//               Text(text = "Denegado")
//           }
//           }
                    if (state.personAccess?.accessDetail == null) {

//       Spacer(modifier = Modifier.height(10.dp))
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                        )
                        Text(
                            text = "Esperando Identificacion ($userChoice)",
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.secondaryVariant
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Button(onClick = {
                            navController.navigate(MainDestination.MANUAL_ROUTE + "/${state.userChoice}") {
                                launchSingleTop = true
                            }
//           viewModel.setAccessPerson()
                        }
                        ) {
                            Text(
                                text = "Marcacion Manual",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.background
                            )
                        }
                    } else {
                        state.personAccess?.let {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp)
                            ) {
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(
                                    text = it.accessState.toString(),
                                    style = MaterialTheme.typography.h5.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colors.background
                                    )
                                )
                                if (it.picture == null) {
                                    Image(
                                        painter = painterResource(id = R.drawable.profile),
                                        contentDescription = "AccessProfile",
                                        modifier = Modifier
                                            .size(250.dp)
                                            .padding(vertical = 30.dp)
                                    )
                                } else {

                                    Image(
                                        bitmap = it.picture.asImageBitmap(),
                                        contentDescription = "AccessProfile",
                                        modifier = Modifier
                                            .size(250.dp)
                                            .padding(vertical = 30.dp)
                                    )
                                }
                                Text(
                                    text = it.personName ?: "N/A",
                                    style = MaterialTheme.typography.h5.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colors.background
                                    ),
                                    maxLines = 2,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                VerticalGrid(modifier = Modifier.padding(5.dp)) {
                                    Text(
                                        text = "Tarjeta", style = MaterialTheme.typography.h5.copy(
                                            color = MaterialTheme.colors.background,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                    Text(
                                        text = it.cardNumber.toString(),
                                        style = MaterialTheme.typography.h5,
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        color = MaterialTheme.colors.background,
                                    )
                                    Text(
                                        text = "Empresa", style = MaterialTheme.typography.h5.copy(
                                            color = MaterialTheme.colors.background,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                    Text(
                                        text = it.empresa ?: "N/A",
                                        style = MaterialTheme.typography.h5,
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        color = MaterialTheme.colors.background,
                                        maxLines = 1,
                                    )
                                    Text(
                                        text = "C.I.", style = MaterialTheme.typography.h5.copy(
                                            color = MaterialTheme.colors.background,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                    Text(
                                        text = it.ci ?: "N/A",
                                        style = MaterialTheme.typography.h5,
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        color = MaterialTheme.colors.background,
                                    )
                                }

                            }
//       Text(text = formatter.formatMediumDate(it.date) )
                        }
                    }
                }
            }
        }
    }
}
