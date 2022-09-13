package com.coppernic.mobility.ui.screens.markings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.coppernic.mobility.domain.observers.ObserverPaginatedMarcaciones
import com.coppernic.mobility.domain.util.MarcacionEstado
import com.coppernic.mobility.domain.util.TipoDeMarcacion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MarkingViewModel @Inject constructor(
    private val pagingInteractor: ObserverPaginatedMarcaciones
):ViewModel(){
    private val sortedOption = MutableStateFlow(false)
    private val tipoDeMarcacion = MutableStateFlow(TipoDeMarcacion.ALL)
    private val marcacionEstado = MutableStateFlow(MarcacionEstado.All)
    private val dateSelect = MutableStateFlow<OffsetDateTime?>(null)

    val pagingState  = pagingInteractor.flow.debounce(250).cachedIn(viewModelScope)



    val state1 :StateFlow<MarkedState> = combine(
        sortedOption,
//        daySortedBy,
//        observerMarcaciones.flow,
        tipoDeMarcacion,
        marcacionEstado,
    ){sortedOption,tipoDeMarcacion,marcacionState->
        MarkedState(
            sortedOptions = sortedOption,
//            dayOptions = dayOption,
//            markers = markers,
            tipoDeMarcacion = tipoDeMarcacion,
            marcacionState = marcacionState
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MarkedState(),
        started = SharingStarted.WhileSubscribed(5000)
    )
    init{
//        viewModelScope.launch {
//            imageDao.getPaginatedMarcaciones().let {
//                Log.d("PAGINATED_RESULT", it.toString())
//            }
//        }
//        pagingInteractor(ObserverMarkers.Params(
//            sorting = false,
//            days = 1,
//            PAGING_CONFIG))
//        observerMarcaciones(ObserverMarcaciones.Params(
//            sortedOption.value,
//            daySortedBy.value,
//            tipoDeMarcacion.value,
//            marcacionEstado.value,
//            dateSelect.value
//        ))
//        observerMarkers(ObserverMarkers.Params(true))
//        viewModelScope.launch {
//            delay(400)
//            observerMarcaciones.flow.collect{
//                state = MarkedState(it)
//            }
//        }

        viewModelScope.launch {
            sortedOption.collect{
                updateDataSource()
            }
        }

        viewModelScope.launch {
            tipoDeMarcacion.collect{
                updateDataSource()
            }
        }
        viewModelScope.launch {
            marcacionEstado.collectLatest {
                updateDataSource()
            }
        }
        viewModelScope.launch {
            dateSelect.collectLatest {
                updateDataSource()
            }
        }


    }
    private fun updateDataSource(){
        pagingInteractor(ObserverPaginatedMarcaciones.Params(
            pagingConfig = PAGING_CONFIG,
            sorted = sortedOption.value,
//            dayOptions = daySortedBy.value,
            tipoDeMarcacion = tipoDeMarcacion.value,
            marcacionEstado = marcacionEstado.value,
            date =  dateSelect.value
        ))
    }
    
    fun setSortedOption(sortedOptions: Boolean){
        viewModelScope.launch {
            this@MarkingViewModel.sortedOption.emit(sortedOptions)
        }
    }

//    fun setSortedOptionDay(dayOptions: dayOptions){
//        viewModelScope.launch {
//            this@MarkingViewModel.daySortedBy.emit(dayOptions)
//        }
//    }

    fun setTipoDeMarcacion(tipoDeMarcacion: TipoDeMarcacion){
        viewModelScope.launch {
            this@MarkingViewModel.tipoDeMarcacion.emit(tipoDeMarcacion)
        }
    }

    fun setMarcacionEstado(typeOfState:MarcacionEstado){
        viewModelScope.launch {
            this@MarkingViewModel.marcacionEstado.emit(typeOfState)
        }
    }
    fun setDateSelect(date:OffsetDateTime?){
        viewModelScope.launch {
            this@MarkingViewModel.dateSelect.emit(date)
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        )
    }
}