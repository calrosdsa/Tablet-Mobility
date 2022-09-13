package com.coppernic.mobility.ui.screens.markings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun FilterRow(
    openFilter:()->Unit,
    sortedState :  MutableState<Boolean>,
    clickSorted:()->Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp)
                .clickable {
                    openFilter()
                }
                .padding(horizontal = 40.dp)
        ) {
            Text(
                text = "Filtros",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "FilterArrowDown")
        }

          Crossfade(targetState = sortedState.value) {
              if(it){
                  Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier
                          .fillMaxWidth(1f)
                          .height(50.dp)
                          .clickable {
                              clickSorted()
                              sortedState.value = !sortedState.value
                          }
                          .padding(horizontal = 30.dp)

                  ) {
                      Text(
                          text = "Ascendente",
                          style = MaterialTheme.typography.subtitle1.copy(
                              fontWeight = FontWeight.Medium
                          ),
                          color = MaterialTheme.colors.onPrimary
                      )
                      Spacer(modifier = Modifier.width(5.dp))
                      Icon(
                          imageVector = Icons.Default.ArrowUpward,
                          contentDescription = " Ascending "
                      )
                  }
              }else{
                  Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.End,
                      modifier = Modifier
                          .fillMaxWidth(1f)
                          .height(50.dp)
                          .clickable {
                              clickSorted()
                              sortedState.value = !sortedState.value
                          }
                          .padding(horizontal = 30.dp)

                  ) {
                  Text(
                      text = "Descendente",
                      style = MaterialTheme.typography.subtitle1.copy(
                          fontWeight = FontWeight.Medium
                      ),
                      color = MaterialTheme.colors.onPrimary
                  )
                  Spacer(modifier = Modifier.width(5.dp))
               Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Descending")
              }
          }
        }

    }
}