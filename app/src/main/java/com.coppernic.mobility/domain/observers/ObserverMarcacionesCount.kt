package com.coppernic.mobility.domain.observers

import com.coppernic.mobility.data.models.dao.MarcacionDao
import com.coppernic.mobility.domain.util.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverMarcacionesCount @Inject constructor(
    private val marcacionDao: MarcacionDao
) : SubjectInteractor<ObserverMarcacionesCount.Params, Int>() {

    override fun createObservable(params: Params): Flow<Int> {
        return marcacionDao.getCountMarcaciones()
    }

    data class Params(
        val unit:Unit
    )
}