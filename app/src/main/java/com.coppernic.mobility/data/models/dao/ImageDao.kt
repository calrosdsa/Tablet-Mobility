package com.coppernic.mobility.data.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.coppernic.mobility.data.models.entities.ImageUser
import com.coppernic.mobility.data.result.CredentialCard
import com.coppernic.mobility.data.result.DetailEntity
import com.coppernic.mobility.data.result.MarcacionWithImage
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
//    @Transaction
//    @Query("SELECT * FROM image_user")
//    fun getCardCredentials(): Flow<List<CredentialCard>>

    @Transaction
    @Query("SELECT * FROM marcacion ORDER BY fecha DESC")
    fun getMarcacionesWithImages():Flow<List<MarcacionWithImage>>



    @Transaction
    @Query("SELECT * FROM image_user WHERE userGui =:guid")
    fun getPersonDetail(guid: String):Flow<DetailEntity>

    @Query("SELECT * FROM IMAGE_USER WHERE userGui = :guid")
    fun getUserImage(guid:String):ImageUser
    @Query("SELECT * FROM image_user")
    fun getUserImages():List<ImageUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(imageUser: ImageUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userImages:List<ImageUser>)
    @Query("DELETE  FROM image_user")
    suspend fun  deleteAllImages()

    @Transaction
    @Query("""
        SELECT * FROM marcacion WHERE fecha BETWEEN :startDate AND :endDate AND
        tipoMarcacion  IN (:type) AND estado IN (:estado)   
         ORDER BY CASE WHEN :isAsc = 1 THEN fecha END ASC, 
        CASE WHEN :isAsc = 0 THEN fecha END DESC
        LIMIT :limit OFFSET :offset
        """)
    fun getPaginatedMarcaciones(
        limit:Int,
        offset:Int,
        isAsc:Boolean,
        startDate:Long,
        endDate:Long,
        type:List<String>,
        estado:List<String>
    ):List<MarcacionWithImage>

//    @Transaction
//    @Query("""
//            SELECT s.* FROM image_user AS s
//            INNER JOIN users_fts AS fts ON s.nombre = fts.docid
//            WHERE fts.name = :filter
//    """)
    @Transaction
    @Query("""
          SELECT * 
            FROM credential 
            INNER JOIN image_user AS image ON credential.guidCardHolder  = image.userGui
            WHERE LOWER(nombre) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == nombre  LIMIT :limit OFFSET :offset
    """)
    suspend fun getPersonalList(query:String,limit:Int,offset: Int): List<CredentialCard>
}

