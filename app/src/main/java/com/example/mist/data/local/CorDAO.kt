package com.example.mist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CorDAO {
    @Query("SELECT * FROM cor")
    fun getAll(): Flow<List<CorEntity>>

    @Query("SELECT * FROM cor WHERE id = :id")
    suspend fun getById(id: Long): CorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(corEntity: CorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cor: List<CorEntity>)

    @Delete
    suspend fun delete(corEntity: CorEntity)
}