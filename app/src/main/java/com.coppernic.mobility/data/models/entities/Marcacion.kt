package com.coppernic.mobility.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "marcacion")
data class Marcacion(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val fecha: Long,
    val date:OffsetDateTime,
    val cardCode: String= "N/A",
    val tipoMarcacion: String? = "N/A",
    val acceso: String? = "N/A",
    val guidUser:String? = "N/A",
//    val imgPerfil: Bitmap? = null,
    val estado: String = "N/A",
//    val nombre: String?,
    val nroTarjeta: Int?,
):AppEntity
