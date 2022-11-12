package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.coppernic.mobility.data.models.entities.Credential
import com.coppernic.mobility.data.result.CredentialCard

@Dao
abstract class UserCardFtsDao {
    @Transaction
    @Query(
        """
        SELECT * FROM credential
        JOIN user_fts  ON credential.guidCardHolder = user_fts.guidCardHolder
        WHERE user_fts MATCH :query LIMIT :limit OFFSET :offset
        """
    )
    abstract suspend fun search(query: String,limit:Int,offset: Int): List<CredentialCard>
}