package com.coppernic.mobility.ui.screens.markings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.coppernic.mobility.ui.rememberFlowWithLifecycle
import com.coppernic.mobility.util.constants.MainDestination

@Composable
fun PaginatedMarcacionesScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel : MarkingPViewModel = hiltViewModel()
){
    val paginItems = rememberFlowWithLifecycle(flow = viewModel.pagingState).collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize()){
        item { 
            Text(text = paginItems.itemCount.toString())
        }
        items(items = paginItems){item->
            MarcacionItem(
                item = item,
                onClick = { navController.navigate(MainDestination.USER_DETAIL + "/${item?.cardImageUser?.userGui}"){
                    launchSingleTop = true
                } }
            )
            Divider()
        }

        if (paginItems.loadState.append == LoadState.Loading) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }

    }
}