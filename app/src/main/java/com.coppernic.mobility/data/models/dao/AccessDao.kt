package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coppernic.mobility.data.models.entities.AccessEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccessDao:DaoEntity<AccessEntity>(){
    @Query("SELECT * FROM access_table")
     abstract fun getAccess(): Flow<AccessEntity>
    @Query("DELETE FROM access_table")
    abstract suspend fun deleteAll()
}