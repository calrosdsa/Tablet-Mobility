package com.coppernic.mobility.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.data.result.CredentialCard
import com.coppernic.mobility.domain.util.PagingInteractor
import com.coppernic.mobility.domain.util.pagination.PaginationUsers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverCardCredential @Inject constructor(
    private val imageDao: ImageDao,
//    private val credentialDao: CredentialDao
): PagingInteractor<ObserverCardCredential.Params, CredentialCard>() {

    override fun createObservable(params: Params): Flow<PagingData<CredentialCard>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                PaginationUsers(
                    params.query,
                    imageDao,
                )
            }
        ).flow
//        return if(params.query == ""){
//           return imageDao.getCardCredentials()
//        }else{
//            imageDao.getCardCredentials().let { result ->
////                result.filter { it[0].cardImage.nombre?.lowercase()?.contains(params.query.lowercase())
////                    ?: true }
//                result.map {cred->
//                    cred.filter { it.cardImage.nombre?.lowercase()?.contains(params.query.lowercase()) ?: true}
//                }
//            }
//
//        }
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val query:String = "a",
    ): Parameters<CredentialCard>
}