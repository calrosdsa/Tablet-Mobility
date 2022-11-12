package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coppernic.mobility.data.models.entities.Config
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {

    @Query("SELECT * FROM config")
    fun getConfig(): List<Config>

    @Query("SELECT * FROM config")
    fun getConfigFlow(): Flow<Config>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConfig(vararg Config: Config)

    @Query("DELETE FROM config")
    suspend fun deleteConfig()

    @Query("UPDATE config set url_servidor=:url where id=:idConfig")
    suspend fun updateUrlServer(url: String, idConfig:Int)

    @Query("UPDATE config set url_servidor=:url, localePass=:pass where id=:idConfig")
    suspend fun updateUrlServerAndPass(url: String, pass:String, idConfig:Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(config: Config)
}