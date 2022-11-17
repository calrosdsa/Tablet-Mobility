package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coppernic.mobility.data.models.entities.Marcacion
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.OffsetDateTime

@Dao
interface MarcacionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMarcacion(vararg Marcacion: Marcacion)

    @Query("DELETE FROM marcacion where fecha NOT IN (SELECT fecha from marcacion ORDER BY fecha DESC LIMIT 500)")
    fun deleteMarcacionesExtra()

    @Query("SELECT * FROM marcacion")
    fun getMarcaciones(): List<Marcacion>

    @Query("SELECT COUNT(id) FROM marcacion WHERE estado = 'pendiente'")
    fun getCountMarcaciones():Flow<Int>

    @Query("SELECT COUNT(id) FROM marcacion WHERE estado = 'pendiente'")
    suspend fun getMarcacionCount():Int


    @Query("SELECT * FROM marcacion ORDER BY fecha DESC")
    fun getMarcacionesFlow(): Flow<List<Marcacion>>

    @Query("SELECT * FROM marcacion WHERE estado='pendiente'")
    suspend fun getMarcacionesPendientes(): List<Marcacion>

    @Query("UPDATE marcacion set estado='enviado' where fecha=:fecha")
    suspend fun setStateMarcacion(fecha:OffsetDateTime)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMarcacion(marcacion: Marcacion)


    @Query("DELETE  FROM marcacion WHERE fecha IN (:fechas)")
    suspend fun deleteMarcaciones (fechas:List<Long?>)

    @Query("DELETE  FROM marcacion WHERE fecha = :fecha")
    suspend fun deleteMarcacion (fecha:Long)

    @Query("SELECT * FROM marcacion WHERE guidUser =:guid ORDER BY fecha DESC LIMIT :limit OFFSET :offset")
//    @Query("SELECT * FROM marcacion WHERE guidUser =:guid LIMIT :limit OFFSET :offset")
    fun getMarcacionesByPerson(guid:String,limit:Int,offset:Int):List<Marcacion>

//    @Query("""
//        SELECT marcacion.nombre AS nombre,marcacion.cardCode AS cardCode,
//        cardHolder.picture AS imgPerfil, marcacion.tipoMarcacion AS tipoMarcacion,
//        marcacion.nroTarjeta As nroTarjeta, marcacion.acceso AS acceso,
//        marcacion.fecha AS fecha
//        FROM marcacion,cardholder WHERE marcacion.nombre = (cardHolder.firstName || cardHolder.lastName)
//    """)
//    suspend fun getMarcacionesWithImages():Flow<List<MarcacionWithImage>>
}