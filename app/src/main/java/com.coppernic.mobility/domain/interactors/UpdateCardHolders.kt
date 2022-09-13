package com.coppernic.mobility.domain.interactors

import com.coppernic.mobility.data.store.CardHolderStore
import com.coppernic.mobility.domain.util.Interactor
import com.coppernic.mobility.domain.util.fetch
import com.coppernic.mobility.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCardHolders @Inject constructor(
    private val cardHolderStore: CardHolderStore,
    private val coroutineDispatchers: AppCoroutineDispatchers,
): Interactor<UpdateCardHolders.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(coroutineDispatchers.io){
            cardHolderStore.fetch(Unit,params.forceRefresh)
        }
    }


    data class Params(
        val forceRefresh: Boolean,
    )

}