package com.example.mist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cor")
data class CorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    val cor: String
)

