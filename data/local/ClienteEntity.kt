package com.example.easyshop.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clientes",
    indices = [Index(value = ["email"], unique = true)]
)
data class ClienteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nomeCompleto: String,

    val email: String
)