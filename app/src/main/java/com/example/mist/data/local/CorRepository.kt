package com.example.mist.data.local

import com.example.mist.domain.Cor
import kotlinx.coroutines.flow.Flow

interface CorRepository {
    fun getAll(): Flow<List<Cor>>

    suspend fun getById(id: Long): Cor

    suspend fun insert(id: Long? = null, nome: String)
}