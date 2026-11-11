package com.example.easyshop.data.nosql

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Produto(
    @Id var id: Long = 0,
    var nome: String = "",
    var preco: Double = 0.0,
    var atributos: MutableMap<String, String> = mutableMapOf()
)
