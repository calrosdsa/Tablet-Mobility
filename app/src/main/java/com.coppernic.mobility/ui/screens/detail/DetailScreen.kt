package com.coppernic.mobility.ui.screens.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.coppernic.mobility.data.models.entities.Marcacion
import com.coppernic.mobility.ui.LocalAppDateFormatter
import com.coppernic.mobility.ui.rememberFlowWithLifecycle
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.R
import org.threeten.bp.OffsetDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailPersonViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val pagingItems = rememberFlowWithLifecycle(flow = viewModel.pagingState).collectAsLazyPagingItems()

    val formatter = LocalAppDateFormatter.current
    Column(modifier =Modifier.fillMaxSize()) {
        IconButton(
            onClick = { navController.popBackStack() }
//            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow_back")
        }
        Divider()
        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            state.detail?.let { detail ->
                item {
                Image(
                    bitmap = detail.cardImage.picture?.asImageBitmap()!!,
                    contentDescription = detail.cardImage.nombre.toString(),
                    modifier = Modifier
                        .padding(20.dp)
                        .size(200.dp)
                        .clip(RoundedCornerShape(50)),
                    contentScale = ContentScale.Fit
                )
                }
                item {

                Text(
                    text = detail.cardImage.nombre.toString(), style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                )
                Divider(
                    Modifier
                        .padding(horizontal = 40.dp, vertical = 5.dp)
                        .height(3.dp)
                        .background(MaterialTheme.colors.primary)
                        .clip(MaterialTheme.shapes.large)
                )
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Informacion", style = MaterialTheme.typography.h6)
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        elevation = 5.dp
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(text = "Edad: 27")
                            Text(text = "Cargo: Ejecutivo de Ventas")
                            Text(text = "Empresa: ${detail.cardHolder.empresa}")
                            Text(text = "C.I.: ${detail.cardHolder.ci}")
                        }
                    }
                }
                item {

                    Text(text = "Marcaciones", style = MaterialTheme.typography.h6)
                    Divider(
                        Modifier
                            .padding(horizontal = 40.dp, vertical = 5.dp)
                            .height(3.dp)
                            .background(MaterialTheme.colors.primary)
                            .clip(MaterialTheme.shapes.large)
                    )
                }
                item {

                    if (pagingItems.itemCount == 0) {
                        Text(
                            text = "No hay marcaciones recientes",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(30.dp)
                        )
                    }
                }
                items(items = pagingItems){item->
                    if(item != null){
                            MarcacionesUser(item = item, format = { fecha ->
                                formatter.formatMediumDateTime(fecha)})
                                Divider()
                    }
            }
            }
        }
    }
}

@Composable
fun MarcacionesUser(
    item:Marcacion,
    format:(OffsetDateTime)->String
){
    Column(modifier = Modifier.padding(7.dp)) {
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Acceso:${if(item.acceso == "false")"Denegado" else "Permitido"}")
            Text(
                text = item.estado,
                color = if (item.estado == "pendiente") Color.Red else Color(0xFF00C853)
            )
//            modifier = Modifier.align(Alignment.End))
        }
        Row(modifier=Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Update, contentDescription = item.cardCode,
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = format(item.date),
                style = MaterialTheme.typography.body2
            )
        }
            Icon(
                painter = painterResource(id = if (item.tipoMarcacion == "R2") R.drawable.p_out else R.drawable.p_in),
                contentDescription = item.tipoMarcacion, modifier = Modifier.size(28.dp),
//                tint = if(item.tipoMarcacion == "R2") Color.Red else Color(0xFF00C853)
            )
        }
    }
}