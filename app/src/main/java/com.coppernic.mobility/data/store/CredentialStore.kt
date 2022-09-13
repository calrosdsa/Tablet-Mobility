package com.coppernic.mobility.data.store

import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.data.result.mapper.toCrendentialEntity
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.data.models.dao.CredentialDao
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

typealias CredentialStore = Store<String,List<Credential>>

@Module
@InstallIn(SingletonComponent::class)
object CredentialModule{
    @Provides
    @Singleton
    fun provideCredentialModule(
        apiService: ApiService,
        credentialDao: CredentialDao
    ):CredentialStore = StoreBuilder.from(
        fetcher = Fetcher.of {_->
            apiService.getCredentials()
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = {_->
                credentialDao.getCredential().map {credentials->
                    when {
                        credentials.isEmpty() -> null
                        else -> credentials
                    }
                }
            },
            writer = {_,entries->
//                Log.d("CREDENTIAL_RESULTS",entries.toString())
            val credentialResults = entries.data.map {
                    it.toCrendentialEntity()}
                credentialDao.deleteAll()
                credentialDao.insertAllCredential(credentialResults)
            },
            delete = credentialDao::deleteById,
            deleteAll = credentialDao::deleteAll
        )
    ).build()
}
