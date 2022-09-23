package com.coppernic.mobility.ui.components

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
) {
    val coroutine = rememberCoroutineScope()


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
        navigationItems.forEach {
            RowIconItem(
                item = it,
                navigateTo = {
                    navController.navigate(it.screen){
                        launchSingleTop = true
                    }
                    coroutine.launch {
                    scaffoldState.drawerState.close()
                    }
                }
            )
        }

    }
}

@Composable
fun RowIconItem(
        item: HomeNavigationItem.ImageVectorIcon,
        navigateTo:()->Unit
    ) {

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

                    Icon(imageVector = item.iconImageVector, contentDescription = item.title,
                    modifier = Modifier.size(31.dp)
//                        tint = MaterialTheme.colors.secondary
                    )
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
