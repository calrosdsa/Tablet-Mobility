package com.coppernic.mobility.domain.util.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.data.result.MarcacionWithImage
import com.coppernic.mobility.domain.util.MarcacionEstado
import com.coppernic.mobility.domain.util.TipoDeMarcacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PaginationMarkers(
    private val imageDao: ImageDao,
    private val sorted:Boolean,
//    val dayOptions: dayOptions,
    val tipoDeMarcacion: TipoDeMarcacion,
    val marcacionEstado: MarcacionEstado,
    private val startDate:Long = 0,
    private val endDate:Long
): PagingSource<Int, MarcacionWithImage>(){
    override fun getRefreshKey(state: PagingState<Int, MarcacionWithImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarcacionWithImage> {
        val page = params.key ?: 0
        val type = if(tipoDeMarcacion == TipoDeMarcacion.INGRESO) listOf("R1") else if(tipoDeMarcacion == TipoDeMarcacion.SALIDA) listOf("R2") else listOf("R1","R2")
//        val type = if(tipoDeMarcacion == TipoDeMarcacion.INGRESO) "R1" else  "R2"
        val estado = if(marcacionEstado == MarcacionEstado.ENVIADO) listOf("enviado") else if(marcacionEstado == MarcacionEstado.PENDIENTE) listOf("pendiente") else listOf("pendiente", "enviado" )


        return try {
//            val dateEpoch = date?.toEpochSecond()
//            val localDate  = OffsetDateTime.of(dateEpoch?.let { LocalDateTime.ofEpochSecond(it,0 , ZoneOffset.UTC) }, ZoneOffset.UTC)
            withContext(Dispatchers.IO){
            val entities = imageDao.getPaginatedMarcaciones(
                limit = params.loadSize,
                offset = page * params.loadSize,
                isAsc = sorted,
                startDate = startDate,
                endDate = endDate,
                type = type,
                estado = estado
            )

//            if (page != 0) delay(1000)
            LoadResult.Page(
                data = entities,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
            }
        } catch (e: Exception) {
            Log.d("MARCACIONES",e.localizedMessage?:"UNEXXPECTED")
            LoadResult.Error(e)
        }
    }

}