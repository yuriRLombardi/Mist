package com.example.mist.data.local

import com.example.mist.domain.Marca
import kotlinx.coroutines.flow.Flow

interface MarcaRepository {
    fun getAll(): Flow<List<Marca>>

    suspend fun getById(id: Long): Marca

    suspend fun insert(id: Long? = null, nome: String)
}