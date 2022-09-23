package com.coppernic.mobility.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun TopBar(
    openMenu:()->Unit,
    onRefresh:()->Unit,
    onOpenDialog:()->Unit,
    marcacionesCount:Int?,
    navigateTo:()->Unit,
    tableName:String,
){
//    var size: Dp by remember { mutableStateOf(40.dp) }

//    val density = LocalDensity.current

    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { onOpenDialog() }) {
            Text(text = tableName, style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxWidth(0.60f),
                maxLines = 1
            )
        }
        Row() {
            IconButton(onClick = { navigateTo() }) {
                BadgedBox(
                    badge = { Badge{ Text(text = marcacionesCount.toString()) }
                    },
                ) {
                    Icon(imageVector = Icons.Default.CardMembership , contentDescription = "refresh_icon",
                        modifier = Modifier
                            .size(30.dp)
                            .zIndex(0f))
                }
            }
            IconButton(onClick = {onRefresh() }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "refresh_icon",
                    modifier = Modifier.size(30.dp))
            }
            IconButton(onClick = { openMenu() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "menu_icon",
                    modifier = Modifier.size(30.dp))
            }
        }
    }
//    }
}

@Composable
fun TopBarComponent(
    openMenu:()->Unit,
    onBack:()->Unit
){
    Row(modifier = Modifier
        .padding(horizontal =  10.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "arrow_back",
                modifier = Modifier.size(30.dp))
        }
            IconButton(onClick = { openMenu() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "menu_icon",
                    modifier = Modifier.size(30.dp))
        }
    }
}