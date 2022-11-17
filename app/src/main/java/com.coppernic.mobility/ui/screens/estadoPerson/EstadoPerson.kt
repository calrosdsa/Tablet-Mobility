package com.coppernic.mobility.ui.screens.estadoPerson

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.coppernic.mobility.R
import com.coppernic.mobility.data.dto.mustering.DataX
import com.coppernic.mobility.ui.LocalAppDateFormatter
import com.coppernic.mobility.ui.components.TopBarComponent
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun EstadoPerson(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: EstadoViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
//    val coroutine = rememberCoroutineScope()
    val focus = LocalFocusManager.current
//    val context = LocalContext.current
//    val defaultImage = BitmapFactory.decodeResource(context.resources, R.drawable.profile)
    state.uiMessage?.let {message->
        LaunchedEffect(key1 = message, block = {
            scaffoldState.snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage()
        })
    }

    val query = remember {
        mutableStateOf("")
    }
    Scaffold(
        modifier=Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {padding1->
        
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(padding1)) {
            Column() {
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
                                viewModel.claerQuery()
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
//                TopBarComponent(openMenu = {
//                    coroutine.launch { scaffoldState.drawerState.open() }
//                }) {
//                    navController.popBackStack()
//                }
//                Divider()
        state.zone.let { zone ->
//                SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = state.loading),
//                    onRefresh = {
//                        viewModel.zoneParam?.let {
//                            viewModel.ciudadParam?.let { it1 ->
//                                viewModel.getMusteringByZoneFoo(
//                                    it.toInt(), it1.toInt()
//                                )
//                            }
//                        }
//                    },
//                    indicator = { state, trigger ->
//                        SwipeRefreshIndicator(
//                            state = state,
//                            refreshTriggerDistance = trigger,
//                            scale = true
//                        )
//                    }
//                ) {
                    zone.let { result ->
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
//                            stickyHeader {
//                                Text(text =  result.first().zona,
//                                style= MaterialTheme.typography.h6,
//                                maxLines = 1,modifier = Modifier
//                                        .fillMaxWidth(0.6f)
//                                        .padding(5.dp))
//                            }
                            items(result) {
                                ZoneItemMarcacion(item = it)
                                Divider()
                            }
                        }
                    }
            if (zone.isEmpty()) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = "No hay Datos disponibles",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                )
            }
            }
            }
        }
    }
//    }
}

@Composable
fun ZoneItemMarcacion(
    item: DataX,
) {
    val formatter = LocalAppDateFormatter.current
    Row(modifier = Modifier.padding(horizontal = 5.dp),
    verticalAlignment = Alignment.Top) {
        if(item.picture.isBlank()){
            Image(
                painter = painterResource(id = R.drawable.profile),
//                painter = rememberAsyncImagePainter(
//                    model = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"),
                contentDescription = item.nombre, modifier = Modifier
                    .size(95.dp)
                    .padding(5.dp)
            )
        }else{
            Image(
                painter = rememberAsyncImagePainter(
                    model = "${formatter.baseUrl}/imagenes/${item.picture}"),
                contentDescription = item.nombre, modifier = Modifier
                    .size(95.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp)),
//                    .clip(MaterialTheme.shapes.small)
            )
        }
        Column(modifier = Modifier.padding(top = 5.dp)) {
            Text(text = item.nombre, maxLines = 1,style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Medium
            ))
//            VerticalGrid {
            Text(text = item.unidadOrganizativa, maxLines = 1,style = MaterialTheme.typography.body2.copy(
                fontSize = 13.sp
            ))
            Text(text = "Lector: ${item.lector}", maxLines = 1,style = MaterialTheme.typography.body2.copy(
                fontSize = 13.sp
            ))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Update, contentDescription = item.fecha,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = item.fecha,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 13.sp
                    )
                )
            }
//            }
//            Text(text = "Zona: ${item.zona}")
//            Text(text = "Unidad Organizativa: ${item.unidadOrganizativa}")
//            Text(text = "Lector: ${item.lector}")
        }
    }
}