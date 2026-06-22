package com.example.mist.data.local

import com.example.mist.domain.Console
import kotlinx.coroutines.flow.Flow

interface PlataformRepository {
    suspend fun insert(
        nome: String,
        preco: Float,
        corId: Long,
        marcaId: Long,
        ano: Long,
        id: Long? = null
    )

    suspend fun delete(id: Long)

    fun getAll(): Flow<List<Console>>

    suspend fun getById(id: Long): Console?
}