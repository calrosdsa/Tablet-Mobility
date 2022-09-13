package com.coppernic.mobility.ui.screens.zones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val coroutine = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        state.zone?.data?.let { zone ->

            Column() {
                TopBarComponent(openMenu = {
                    coroutine.launch { scaffoldState.drawerState.open() }
                }) {
                    navController.popBackStack()
                }
                Divider()
                SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = state.loading),
                    onRefresh = {
                        viewModel.zoneParam?.let {
                            viewModel.ciudadParam?.let { it1 ->
                                viewModel.getMusteringByZoneFoo(
                                    it.toInt(), it1.toInt()
                                )
                            }
                        }
                    },
                    indicator = { state, trigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = trigger,
                            scale = true
                        )
                    }
                ) {
                    zone.let { result ->
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(result) {
                                ZoneItemMarcacion(item = it)
                                Divider()
                            }
                        }
                    }
                }
            }
            if (zone.isEmpty()) {
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

