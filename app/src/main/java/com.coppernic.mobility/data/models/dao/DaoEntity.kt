package com.coppernic.mobility.data.models.dao

import androidx.room.*
import com.coppernic.mobility.data.models.entities.AppEntity


abstract class DaoEntity<in E:AppEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: E): Long

    @Insert
    abstract suspend fun insertAll(vararg entity: E)

    @Insert
    abstract suspend fun insertAll(entities: List<E>)

    @Update
    abstract suspend fun update(entity: E)

    @Delete
    abstract suspend fun deleteEntity(entity: E): Int

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()

    suspend fun insertOrUpdate(entity: E): Long {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(entities: List<E>) {
        entities.forEach {
            insertOrUpdate(it)
        }
    }
}