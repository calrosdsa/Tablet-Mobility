package com.coppernic.mobility.domain.interactors

import com.coppernic.mobility.data.store.CredentialStore
import com.coppernic.mobility.domain.util.Interactor
import com.coppernic.mobility.domain.util.fetch
import com.coppernic.mobility.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCredentials @Inject constructor(
    private val credentialStore: CredentialStore,
    private val coroutineDispatchers: AppCoroutineDispatchers,
    ) : Interactor<UpdateCredentials.Params>(){

    override suspend fun doWork(params: Params) {
        withContext(coroutineDispatchers.io){
            credentialStore.fetch("",params.forceRefresh)
        }
    }

    data class Params(
        val forceRefresh:Boolean = false
    )
}