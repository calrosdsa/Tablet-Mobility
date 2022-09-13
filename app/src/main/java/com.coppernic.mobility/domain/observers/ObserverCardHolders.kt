package com.coppernic.mobility.domain.observers

import com.coppernic.mobility.data.models.entities.Cardholder
import com.coppernic.mobility.data.models.dao.CardholderDao
import com.coppernic.mobility.domain.util.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverCardHolders @Inject constructor(
    private val cardholderDao: CardholderDao
): SubjectInteractor<ObserverCardHolders.Params, List<Cardholder>>() {

    override fun createObservable(params: Params): Flow<List<Cardholder>> {
       return cardholderDao.getCardholder()
    }

    data class Params(
        val id:Int? = null
    )
}