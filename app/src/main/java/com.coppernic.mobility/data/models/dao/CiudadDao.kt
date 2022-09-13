package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coppernic.mobility.data.models.entities.Ciudad

@Dao
interface CiudadDao {

    @Query("SELECT * FROM ciudad")
    suspend fun getCiudades(): List<Ciudad>

    suspend fun insertCiudades(Ciudades: List<Ciudad>){
        deleteAll()
        insertCiudades2(Ciudades)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCiudades2(Ciudades: List<Ciudad>)

    @Query("DELETE FROM ciudad")
    fun deleteAll()
}