package com.coppernic.mobility.data.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coppernic.mobility.data.dto.access.AccessValue

@Entity(
    tableName = "access_table"
)
data class AccessEntity(
    @PrimaryKey(autoGenerate = true)override val id:Long = 0,
    @ColumnInfo(name="access_settings") val accessSettings:List<AccessValue> = emptyList(),
    @ColumnInfo(name ="access_inicio") val accessInicio :List<AccessValue> = emptyList(),
):AppEntity