package com.example.mist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marca")
data class MarcaEntity (
    @PrimaryKey(autoGenerate = true) val id:Long=1,
    val marca:String
)
