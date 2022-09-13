package com.coppernic.mobility.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credential")
data class Credential(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val guid: String = "12sa",
    val guidCardHolder: String? = "N/A",
    val cardNumber: Int? = null ,
    val facilityCode: Int? = null,
    val uniqueId: String? = "N/A",
    val estado: String? = "N/A",
):AppEntity