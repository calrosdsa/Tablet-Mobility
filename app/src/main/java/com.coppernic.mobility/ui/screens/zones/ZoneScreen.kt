package com.coppernic.mobility.ui.screens.zones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coppernic.mobility.ui.components.TopBarComponent
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.ui.screens.estadoPerson.ZoneItemMarcacion
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun ZoneScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: ZonesViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
//    val coroutine = rememberCoroutineScope()
    val focus = LocalFocusManager.current
    val query = remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TextField(
                        value = query.value,
                        onValueChange = {
                            query.value = it
                            viewModel.search(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Buscar...") },
                        leadingIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "manual_marked_back"
                                )
                            }
                        },
                        trailingIcon = {
                            if (query.value.isNotBlank()) {
                                IconButton(onClick = {
                                    query.value = ""
                                    focus.clearFocus()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "manual_marked_back"
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focus.clearFocus()
                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
//                textColor = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.7f),
                        ),
                    )
                },
                modifier = Modifier.fillMaxSize()) { padding ->
                Column(modifier = Modifier.padding(padding)) {
//                    TopBarComponent(openMenu = {
//                        coroutine.launch { scaffoldState.drawerState.open() }
//                    }) {
//                        navController.popBackStack()
//                    }
                    state.zone.let { zone ->
//                    Divider()
//                    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = state.loading),
//                        onRefresh = {
//                            viewModel.zoneParam?.let {
//                                viewModel.ciudadParam?.let { it1 ->
//                                    viewModel.getMusteringByZoneFoo(
//                                        it.toInt(), it1.toInt(),
//                                    )
//                                }
//                            }
//                        },
//                        indicator = { state, trigger ->
//                            SwipeRefreshIndicator(
//                                state = state,
//                                refreshTriggerDistance = trigger,
//                                scale = true
//                            )
//                        }
//                    ) {
                        zone.let { result ->
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(result) {
                                    ZoneItemMarcacion(item = it)
                                    Divider()
                                }
                            }
                        }
//                    }
                }
//                if (zone.isEmpty()) {
//                    Text(
//                        text = "No hay Datos disponibles",
//                        style = MaterialTheme.typography.h6,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                    )
//                }
            }
        }
    }
}

