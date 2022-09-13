package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coppernic.mobility.data.models.entities.Cardholder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CardholderDao: DaoEntity<Cardholder>(){

    @Query("SELECT * FROM cardholder")
    abstract fun getCardholder(): Flow<List<Cardholder>>

    @Query("SELECT * FROM cardholder")
    abstract suspend fun getCardholder2(): List<Cardholder>

    suspend fun insertCardholders(Cardholders: List<Cardholder>){
        deleteAll()
        insertCardholders2(Cardholders)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   abstract suspend fun insertCardholders2(Cardholders: List<Cardholder>)

    @Query("DELETE FROM cardholder")
    abstract fun deleteAll()


    @Query("SELECT * FROM cardholder WHERE guid = :g")
    abstract fun getCardholderByGuid(g: String): Cardholder?

    @Query("DELETE FROM cardholder WHERE guid = :g")
    abstract fun deleteById(g: String)

}