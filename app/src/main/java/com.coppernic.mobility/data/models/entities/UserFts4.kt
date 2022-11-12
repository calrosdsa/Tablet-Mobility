package com.coppernic.mobility.data.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "user_fts")
@Fts4(contentEntity = Credential::class)
data class UserFts4(
    @ColumnInfo(name = "guidCardHolder") val guidCardHolder: String? = null,
//    @ColumnInfo(name = "original_title") val originalTitle: String? = null
)
