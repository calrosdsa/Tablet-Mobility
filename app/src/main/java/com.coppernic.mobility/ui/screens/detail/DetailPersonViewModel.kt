package com.coppernic.mobility.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.data.models.dao.MarcacionDao
import com.coppernic.mobility.data.models.entities.Marcacion
import com.coppernic.mobility.data.result.DetailEntity
import com.coppernic.mobility.domain.util.pagination.PaginationUserMarcaciones
import com.coppernic.mobility.util.constants.Params
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPersonViewModel @Inject constructor(
    private val imageDao: ImageDao,
    private val marcacionDao: MarcacionDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val guid = savedStateHandle.get<String>(Params.GUID_USER)
    val pagingState = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            PaginationUserMarcaciones(
                marcacionDao = marcacionDao,
                guid = guid?:"N/A"
            )
        }
    ).flow.cachedIn(viewModelScope)
    val markers = MutableStateFlow<List<Marcacion>>(emptyList())
    private val detail = MutableStateFlow<DetailEntity?>(null)
    val state: StateFlow<DetailPersonState> = combine(
        detail
    ) { userDetail ->
        DetailPersonState(
            detail = userDetail[0],
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailPersonState()
    )

    init {
        guid?.let {
            viewModelScope.launch {
                imageDao.getPersonDetail(it).collect { detailP ->
                    this@DetailPersonViewModel.detail.emit(detailP)
                }
            }
//            viewModelScope.launch {
//                marcacionDao.getMarcacionesByPerson(it).collect { markings ->
//                    delay(400)
//                    this@DetailPersonViewModel.markers.emit(markings)
//                }
//            }
        }
    }
}