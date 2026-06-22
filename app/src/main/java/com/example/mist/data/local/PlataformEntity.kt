package com.example.mist.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "console",
    foreignKeys = [
        ForeignKey(
            entity = CorEntity::class,
            parentColumns = ["id"],
            childColumns = ["cor"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MarcaEntity::class,
            parentColumns = ["id"],
            childColumns = ["marca"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlataformEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val preco: Float,
    @ColumnInfo(name = "cor") val corId: Long,
    @ColumnInfo(name = "marca") val marcaId: Long,
    val ano: Long
)
