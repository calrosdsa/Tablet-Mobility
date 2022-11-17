package com.coppernic.mobility.ui.screens.markings

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.coppernic.mobility.data.result.MarcacionWithImage
import com.coppernic.mobility.ui.LocalAppDateFormatter
import com.coppernic.mobility.ui.components.TopBarComponent
import com.coppernic.mobility.ui.rememberFlowWithLifecycle
import com.coppernic.mobility.ui.rememberStateWithLifecycle
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.R
import com.coppernic.mobility.ui.components.ImageUserComponent
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MarkedsScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: MarkingViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state1)
    val pagingItems = rememberFlowWithLifecycle(flow = viewModel.pagingState).collectAsLazyPagingItems()
//    var marcacionMessage by remember {
//        mutableStateOf("")
//    }

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val lazyState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    val setSorted = remember {
        mutableStateOf(false)
    }
    var dateValue by remember {
        mutableStateOf("")
    }
    val mContext = LocalContext.current
    val defaultImage = BitmapFactory.decodeResource(mContext.resources, R.drawable.profile)


    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
//    val mDate = remember { mutableStateOf("") }
    val zone: ZoneId = ZoneId.of("GMT-4")
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            dateValue = "${mDayOfMonth}/${mMonth + 1}/$mYear"
            val date = LocalDateTime.of(mYear, mMonth + 1, mDayOfMonth, 0, 0, 0)
            val zoneOffSet: ZoneOffset = zone.rules.getOffset(date)
            val time = date.atOffset(zoneOffSet)
            viewModel.setDateSelect(time)
        }, mYear, mMonth, mDay
    )



    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                MarkerBottomSheetContent(
//                    currentDayOption = state.dayOptions ?: dayOptions.TODAY,
                    selectTipoDeMarcacion = {
                        viewModel.setTipoDeMarcacion(it)
                    },
                    datePickerDialog = mDatePickerDialog,
                    currentTipoDeMarcacion = state.tipoDeMarcacion,
                    currentMarcacionEstado = state.marcacionState,
                    setMarcacionEstado = { viewModel.setMarcacionEstado(it) },
                    setDateCalendar = {
                        viewModel.setDateSelect(null)
                        dateValue = ""
                    },
                    setFilters = {
                        viewModel.updateDataSource()
                        coroutine.launch {
                            sheetState.hide()
                        }
                    },
                    dateValue = dateValue
                )
            }
        }, sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colors.surface,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxSize(),
        sheetShape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
    ) {
        Scaffold(
            topBar = {
                TopBarComponent(openMenu = {
                    coroutine.launch {
                        scaffoldState.drawerState.open()
                    }
                }, onBack = { navController.popBackStack() })
                Divider()
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutine.launch {
                        lazyState.scrollToItem(0)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = " Ascending "
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    FilterRow(openFilter = { coroutine.launch { sheetState.show() } },
                        sortedState = setSorted,
                        clickSorted = {
                            if (state.sortedOptions) {
                                viewModel.setSortedOption(false)
                            } else viewModel.setSortedOption(true)
                        }
                    )
                    LazyColumn(state = lazyState, modifier = Modifier.fillMaxSize()) {
                        items(
                            items = pagingItems,
                            key = {it.marcacion?.id?: 0 }
                        ) { item ->
                            MarcacionItem(
                                item = item,
                                onClick = {
                                    navController.navigate(MainDestination.USER_DETAIL + "/${item?.cardImageUser?.userGui}") {
                                        launchSingleTop = true
                                    }
                                },
                                defaultImage = defaultImage
                            )
                            Divider()
                        }
                    }
                }

//                if (marcacionMessage != "") {
//                    val formatter = LocalAppDateFormatter.current
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                    ) {
//                        Column(modifier = Modifier.align(Alignment.Center)) {
//                            Text(
//                                text = marcacionMessage,
//                                style = MaterialTheme.typography.subtitle1,
//                                modifier = Modifier.padding(10.dp),
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.height(5.dp))
//                            Text(
//                                text = "Tipo de Marcación: ${state.tipoDeMarcacion.name}",
//                                style = MaterialTheme.typography.subtitle1,
//                                modifier = Modifier.padding(10.dp),
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.height(5.dp))
//                            Text(
//                                text = "Estado de la Marcación: ${state.marcacionState.name  }",
//                                style = MaterialTheme.typography.subtitle1,
//                                modifier = Modifier.padding(10.dp),
//                                textAlign = TextAlign.Center
//                            )
//                            state.dateSelect?.let { dateOffset->
//                            Spacer(modifier = Modifier.height(5.dp))
//                                val date = dateOffset.toLocalDateTime()
//                            Text(
//                                text = "Fecha:${formatter.formatMediumDateTime(date)} ",
//                                style = MaterialTheme.typography.subtitle1,
//                                modifier = Modifier.padding(10.dp),
//                                textAlign = TextAlign.Center
//                            )
//                            }
//                        }
//                    }
//                }
        }
    }
}
@Composable
fun MarcacionItem(
    item: MarcacionWithImage?,
    onClick:()->Unit,
    defaultImage:Bitmap,
) {
    val formatter = LocalAppDateFormatter.current
//    val date = formatter.

    item?.marcacion?.let { marcacion ->

        Row(verticalAlignment = Alignment.Top, modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(5.dp)) {
//            if(item.cardImageUser.picture == null){
//                Image(
//                    painter = painterResource(id = R.drawable.profile),
//                    contentDescription = item.marcacion.cardCode,
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier
//                        .size(90.dp, 100.dp)
//                )
//            }else{
//                Image(
//                    bitmap = item.cardImageUser.picture?.asImageBitmap()?:defaultImage.asImageBitmap(),
//                    contentDescription = item.marcacion.cardCode,
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier
//                        .size(90.dp, 90.dp)
//                )
            if(item.cardImageUser == null){
                Image(
                    bitmap = defaultImage.asImageBitmap(),
                    contentDescription = item.marcacion!!.cardCode,
                    modifier = Modifier
                        .size(90.dp, 90.dp)
                )
//                ImageUserComponent(
//                    model = defaultImage,
//                    description = item.marcacion!!.cardCode,
//                    modifier = Modifier
//                        .size(90.dp, 90.dp)
//                )
            }else{
            ImageUserComponent(
                model = item.cardImageUser?.picture,
                description = item.marcacion!!.cardCode,
                modifier = Modifier
                    .size(90.dp, 90.dp)
            )
            }
//            }
            Spacer(modifier = Modifier.width(5.dp))
            Column() {
                    Text(text = item.cardImageUser?.nombre ?:"Visita",style = MaterialTheme.typography.subtitle1,
                        maxLines = 1)
                Spacer(modifier = Modifier.height(2.dp))
                Row(modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "Codigo:${marcacion.nroTarjeta}",
                        style = MaterialTheme.typography.body2,
                        maxLines = 1
                    )
//                    Text(
//                        text = "Numero: ${marcacion.nroTarjeta}",
//                        text = "Numero:${marcacion.}",
//                        style = MaterialTheme.typography.body2,
//                        maxLines = 1
//                    )
                    Text(
//                        text = "Estado: ${if (marcacion.tipoMarcacion == "R1") "Ingreso" else "Salida"}",
                        text = marcacion.estado,
                        style = MaterialTheme.typography.body2,
                        color =if(marcacion.estado == "pendiente") Color.Red else Color(0xFF00C853)
                    )
                }
                Row(modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Acceso:${if (marcacion.acceso == "true") "Acceso Permitido" else "Acceso Denegado"}",
                        style = MaterialTheme.typography.body2,
                        maxLines = 1
                    )
                    Text(
                        text =if (marcacion.tipoMarcacion == "R1") "Ingreso" else "Salida",
                        style = MaterialTheme.typography.body2,
                        color =if(marcacion.estado == "pendiente") Color.Red else Color(0xFF00C853)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Update, contentDescription = marcacion.cardCode,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = formatter.formatMediumDateTime(marcacion.date),
                            style = MaterialTheme.typography.body2
                        )
                    }
//                Icon(imageVector =if(marcacion.tipoMarcacion == "R1")  Icons.Default.Check else Icons.Default.Close,
//                    contentDescription = marcacion.estado)
                    Icon(
                        painter = painterResource(id = if (marcacion.tipoMarcacion == "R1") R.drawable.p_in else R.drawable.p_out),
                        contentDescription = marcacion.tipoMarcacion, modifier = Modifier.size(28.dp),
                        tint = if(marcacion.estado == "pendiente") Color.Red else Color(0xFF00C853)
                    )
                }
            }
        }
    }
}


//fun org.threeten.bp.LocalDate.toLocalDateOrg():LocalDate{
//
//}