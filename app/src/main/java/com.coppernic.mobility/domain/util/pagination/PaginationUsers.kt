package com.coppernic.mobility.domain.util.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.data.models.dao.UserCardFtsDao
import com.coppernic.mobility.data.result.CredentialCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaginationUsers (
    private val query:String = "",
//    private val userCardFtsDao: UserCardFtsDao
    private val imageDao: ImageDao
) : PagingSource<Int, CredentialCard>(){
    override fun getRefreshKey(state: PagingState<Int, CredentialCard>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CredentialCard> {
        val page = params.key ?: 0
        return try {
//            val dateEpoch = date?.toEpochSecond()
//            val localDate  = OffsetDateTime.of(dateEpoch?.let { LocalDateTime.ofEpochSecond(it,0 , ZoneOffset.UTC) }, ZoneOffset.UTC)
            withContext(Dispatchers.IO){
                val entities = imageDao.getPersonalList(
                    query = query,
                    limit = params.loadSize,
                    offset = page * params.loadSize,
                )
//                val entities = userCardFtsDao.search(
//                    query = "*$query*",
//                    limit = params.loadSize,
//                    offset = page * params.loadSize,
//                )
//            if (page != 0) delay(1000)
                LoadResult.Page(
                    data = entities,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (entities.isEmpty()) null else page + 1
                )
            }
        } catch (e: Throwable) {
            Log.d("MARCACIONES",e.localizedMessage?:"UNEXXPECTED")
            LoadResult.Error(e)
        }
    }
}