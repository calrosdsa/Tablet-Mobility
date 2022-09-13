package com.coppernic.mobility.domain.observers

import com.coppernic.mobility.data.models.dao.ConfigDao
import com.coppernic.mobility.data.models.entities.Config
import com.coppernic.mobility.domain.util.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverSettings @Inject constructor(
    private val configDao: ConfigDao
) : SubjectInteractor<ObserverSettings.Params,Config>() {

    override fun createObservable(params: Params): Flow<Config> {
        return configDao.getConfigFlow()
    }

    data class Params(
        val unit:Unit
    )
}