package com.coppernic.mobility.ui.screens.usuarios

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.coppernic.mobility.R
import com.coppernic.mobility.ui.rememberFlowWithLifecycle
import com.coppernic.mobility.ui.screens.manual_marker.CardCredentialItem
import com.coppernic.mobility.ui.screens.manual_marker.ManualMarkerViewModel
import com.coppernic.mobility.util.constants.MainDestination

@Composable
fun UsersScreem(
    navController: NavController,
    viewModel: ManualMarkerViewModel = hiltViewModel()
) {
    val pagingItems =
        rememberFlowWithLifecycle(flow = viewModel.pagingItems).collectAsLazyPagingItems()
//    val query by rememberStateWithLifecycle(stateFlow = viewModel.query)
    val focus = LocalFocusManager.current
//    val context = LocalContext.current
//    val defaultImage = BitmapFactory.decodeResource(context.resources, R.drawable.profile)

    val query = remember {
        mutableStateOf("")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {padding->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 10.dp)
            ) {
                items(items = pagingItems) { item ->
                    CardCredentialItem(
                        item1 = item,
                        onClick = {
                            navController.navigate(MainDestination.USER_DETAIL + "/${item?.cardImage?.userGui}") {
                                launchSingleTop = true
                            }
                        },
//                        defaultImage = defaultImage
                    )
                    Divider()
                }
            }
        }
    }
}
