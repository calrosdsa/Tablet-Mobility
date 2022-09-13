package com.coppernic.mobility.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.coppernic.mobility.util.HomeNavigationItem
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.util.navigationItems
import kotlinx.coroutines.launch

@Composable
fun DrawerContentScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    currentSelected: String,
    passwordPref:String
) {
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


    Column(
        modifier = Modifier
            .padding(vertical =  10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { coroutine.launch { 
                scaffoldState.drawerState.close()
            } }) {
                Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "Drawer Cancel")
            }
        }
        navigationItems.forEachIndexed() {index,it->
            RowIconItem(
                item = it,
                selected = currentSelected == it.screen,
                navigateTo = {
                    if(index != 5){
                    navController.navigate(it.screen){
                        launchSingleTop = true
                    }
                    coroutine.launch {
                    scaffoldState.drawerState.close()
                    }
                    }else{
                        deleteAlert = true
                    }
                }
            )
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
                            if(passwordPref == password.value){
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
                        if(passwordPref == password.value){
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
                    }) {
                        Text(text = "INGRESAR",color = MaterialTheme.colors.primary)
                    }
                }

            }
        }
    }
}

@Composable
fun RowIconItem(
        item: HomeNavigationItem.ImageVectorIcon,
        selected: Boolean,
        navigateTo:()->Unit
    ) {
        val painter = rememberVectorPainter(image = item.iconImageVector)
        val selectPainter = item.selectedImageVector?.let { rememberVectorPainter(image = it) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateTo() }
                .padding(vertical = 10.dp, horizontal = 18.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectPainter != null) {
                    Crossfade(targetState = selected) {
                        Icon(
                            painter = if (it) selectPainter else painter,
                            contentDescription = item.title,
                            modifier = Modifier.size(31.dp),
//                            tint = MaterialTheme.colors.secondary
                        )
                    }
                } else {
                    Icon(painter = painter, contentDescription = item.title,
                    modifier = Modifier.size(31.dp)
//                        tint = MaterialTheme.colors.secondary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = item.title, style = MaterialTheme.typography.subtitle1)
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "arrow ${item.title}"
            )
        }
    Divider(Modifier.padding())
    }
