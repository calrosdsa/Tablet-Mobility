package com.coppernic.mobility.ui.screens.manual_marker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.coppernic.mobility.data.result.CredentialCard
import com.coppernic.mobility.ui.components.DialogConfirmation
import com.coppernic.mobility.ui.components.ImageUserComponent
import com.coppernic.mobility.ui.components.VerticalGrid
import com.coppernic.mobility.ui.rememberFlowWithLifecycle

@Composable
fun ManualMarkerScreen(
    navController: NavController,
    viewModel: ManualMarkerViewModel = hiltViewModel()
) {
    val pagingItems =
        rememberFlowWithLifecycle(flow = viewModel.pagingItems).collectAsLazyPagingItems()
    val focus = LocalFocusManager.current
    val query = remember {
        mutableStateOf("")
    }
    var alertDialog by remember {
        mutableStateOf(false)
    }
    var currentItem by remember {
        mutableStateOf<CredentialCard?>(null)
    }
//    val context = LocalContext.current
//    val defaultImage = BitmapFactory.decodeResource(context.resources, R.drawable.profile)

//    LaunchedEffect(key1 = viewModel, block = {
//        Log.d("CHECK_DATA",state.cardCredential.toString())
//        Log.d("ACCESS_STATE","MANUAL_SCREEN ${navController.currentDestination?.id}")
//    })
    if (alertDialog) {
        DialogConfirmation(onDismiss = { alertDialog = false }) {
            alertDialog = false
            currentItem?.let { it.credential?.let { it1 -> viewModel.sendManualConfirmation(it1, navController) } }
        }

    }
    Scaffold(modifier = Modifier.fillMaxSize()) {padding ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
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
                items(
                    items = pagingItems,
                    key = { it.credential?.id ?: 0 }
                ) { item ->
                    CardCredentialItem(
                        item1 = item,
                        onClick = {
                            alertDialog = true
                            currentItem = item
                        },
//                        defaultImage = defaultImage
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun CardCredentialItem(
    item1: CredentialCard?,
    onClick:()->Unit,
//    defaultImage:Bitmap
){
    item1?.credential.let {item->

    Row(verticalAlignment = Alignment.Top,modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        }
        .padding(5.dp) ) {
//        if(item1.cardImage.picture == null){
//
//            Image(painter = painterResource(id = R.drawable.profile), contentDescription = item.guidCardHolder,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .size(90.dp,100.dp)
//            )
//        }else{
//        Image(
//            bitmap = item1?.cardImage?.picture?.asImageBitmap()?:defaultImage.asImageBitmap(),
//            contentDescription = item?.cardNumber.toString(),
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier
//                .size(90.dp,90.dp)
//        )
//        Image(
//            painter = rememberAsyncImagePainter(model =
//            "http://10.0.1.181:12015/imagenes/${item1?.cardImage?.picture}"
//            ),
//            contentDescription = item?.guid,
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier
//                .size(90.dp,90.dp)
//        )
        ImageUserComponent(
            model = item1?.cardImage?.picture,
            description = item?.guid,
            modifier = Modifier
                .size(90.dp,90.dp)
            ,
            requestBuilder = { crossfade(true) },
            )
//        }
        Spacer(modifier = Modifier.width(15.dp))
        Column() {
            Text(text = item1?.cardImage?.nombre.toString(),maxLines = 1)
            Spacer(modifier = Modifier.height(5.dp))
            VerticalGrid() {
                Text(text = "Estado: ${item?.estado}", style = MaterialTheme.typography.body2)
                Text(text = "Código: ${item?.facilityCode}", style = MaterialTheme.typography.body2)
//                Text(text = "C.I: ${item.ci}", style = MaterialTheme.typography.body2)
            }
                Text(text = "Número de Tarjeta: ${item?.cardNumber}", style = MaterialTheme.typography.body2)
        }

    }
    }
}
