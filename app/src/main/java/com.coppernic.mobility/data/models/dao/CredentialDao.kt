package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coppernic.mobility.data.models.entities.Credential
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CredentialDao : DaoEntity<Credential>(){

    @Query("SELECT * FROM credential")
     abstract fun getCredential(): Flow<List<Credential>>

    @Query("SELECT * FROM credential")
    abstract fun getCredentialList(): List<Credential>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertCredential(vararg Credential: Credential)

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllCredential(credentials:List<Credential>)


    @Query("DELETE FROM credential")
    abstract fun deleteAll()

    @Query("SELECT * FROM credential WHERE facilityCode = :fc AND cardNumber = :cn")
    abstract fun getCredentialByNumber(fc: Int, cn: Int): Credential?

    @Query("SELECT * FROM credential WHERE guidCardHolder = :guid")
    abstract fun getCredsByCardholderGuid(guid: String): List<Credential>

    @Query("DELETE FROM credential WHERE guidCardHolder = :g")
    abstract fun deleteById(g: String)

//    @Query("""
//        SELECT cardholder.firstName AS firstName,cardHolder.lastName AS lastName,
//        cardHolder.picture AS picture, cardHolder.guid AS guidCardHolder,
//        credential.cardNumber AS cardNumber,credential.facilityCode AS facilityCode,
//        cardHolder.estado AS estado, cardHolder.ci AS ci
//        FROM credential,cardholder WHERE cardHolder.guid = credential.guidCardHolder
//    """)
//    @Transaction
//    @Query("SELECT * FROM image_user")
//    abstract fun getCardCredentials():Flow<List<CredentialCard>>

}