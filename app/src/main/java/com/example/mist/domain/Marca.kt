package com.example.mist.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Marca(@PrimaryKey val id: Long, val marca: String)