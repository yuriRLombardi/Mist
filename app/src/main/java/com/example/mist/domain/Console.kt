package com.example.mist.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Console(
    val id: Long,
    val nome: String,
    val preco: Float,
    val cor: Cor,
    val marca: Marca,
    val ano: Long,
)
