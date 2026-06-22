package com.example.mist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MarcaDAO {
    @Query("SELECT * FROM marca")
    fun getAll(): Flow<List<MarcaEntity>>

    @Query("SELECT * FROM marca WHERE id = :id")
    suspend fun getById(id: Long): MarcaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marcaEntity: MarcaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cor: List<MarcaEntity>)

    @Delete
    suspend fun delete(marcaEntity: MarcaEntity)
}