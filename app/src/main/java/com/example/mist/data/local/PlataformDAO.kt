package com.example.mist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlataformDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plataformEntity: PlataformEntity)

    @Delete
    suspend fun delete(plataformEntity: PlataformEntity)

    @Query("SELECT * FROM console")
    fun getAll(): Flow<List<PlataformEntity>>

    @Query("SELECT * FROM console WHERE id = :id")
    suspend fun getById(id: Long): PlataformEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plataform: List<PlataformEntity>)

    @Query("DELETE FROM console")
    suspend fun deleteAll()

}