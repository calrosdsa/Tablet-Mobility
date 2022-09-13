package com.coppernic.mobility.domain.observers

import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.data.result.MarcacionWithImage
import com.coppernic.mobility.domain.util.*
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import javax.inject.Inject


class ObserverMarcaciones @Inject constructor(
    private val imageDao: ImageDao,
): SubjectInteractor<ObserverMarcaciones.Params, List<MarcacionWithImage>>() {
    private val zone: ZoneId = ZoneId.systemDefault()
    private val localDateTime = LocalDateTime.now()
    private val zoneOffSet: ZoneOffset = zone.rules.getOffset(localDateTime)
    private val offsetDateTime: OffsetDateTime = localDateTime.atOffset(zoneOffSet)

    override fun createObservable(params: Params): Flow<List<MarcacionWithImage>> {
        return imageDao.getMarcacionesWithImages()
    }

    data class Params(
        val sorted:Boolean,
        val dayOptions: dayOptions,
        val tipoDeMarcacion:TipoDeMarcacion,
        val marcacionEstado: MarcacionEstado,
        val date:OffsetDateTime? = null
    )

}