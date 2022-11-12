package com.coppernic.mobility.tasks

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coppernic.mobility.data.models.dao.MarcacionDao
import com.coppernic.mobility.domain.extensions.inPast
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.threeten.bp.*
import org.threeten.bp.temporal.TemporalAmount

@HiltWorker
class DeleteMarcacionesWorker @AssistedInject constructor(
    @Assisted context:Context,
    @Assisted params:WorkerParameters,
    private val marcacionDao: MarcacionDao,
) :CoroutineWorker(context,params){

    companion object {
        const val TAG = "delete_marcaciones_worker"
    }

    override suspend fun doWork(): Result {
//        Log.d("ACCION_RESULT", "BEGIN_WORK")
        try{
//            val zone: ZoneId = ZoneId.systemDefault()
//            val localDateTime = LocalDateTime.now()
//            val zoneOffSet: ZoneOffset = zone.rules.getOffset(localDateTime)
//            val offsetDateTime: OffsetDateTime = localDateTime.atOffset(zoneOffSet)
            val marcaciones = marcacionDao.getMarcaciones()
            val instant = Duration.ofDays(31) as TemporalAmount
            val past = instant.inPast().epochSecond
            val resultM =  marcaciones.filter {
                it.fecha < past
//                val result =  it.fecha.isBefore(offsetDateTime)
//                Log.d("ACCION_RESULT",  result.toString())
//                val result1 = it.fecha.isBefore(past)
//                Log.d("ACCION_RESULT2",  result1.toString())
            }.map {
//                Log.d("ACCION_RESULT2", it.toString())
            marcacionDao.deleteMarcacion(it.fecha)
                it.fecha
            }
//            Log.d("ACCION_RESULT",  resultM.size.toString())
        }catch(e:Throwable){
//            Log.d("ACCION_RESULT", "ERROR IN WORK")
//            Log.d("ACCION_RESULT", e.localizedMessage?:"Unexpected")
            return Result.failure()
//                Log.d("TASK_STATE","Something goes wrong")

        }
        return Result.success()
    }


}