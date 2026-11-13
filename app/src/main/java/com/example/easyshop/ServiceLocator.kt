package com.example.easyshop

import android.content.Context
import com.example.easyshop.data.local.EasyShopDatabase
import com.example.easyshop.data.nosql.ObjectBox
import com.example.easyshop.data.nosql.Produto
import com.example.easyshop.data.nosql.ProdutoRepository
import com.example.easyshop.data.repository.ClienteRepository
import com.example.easyshop.service.ClienteService
import com.example.easyshop.service.ClienteServiceImpl
import com.example.easyshop.service.ProdutoService
import com.example.easyshop.service.ProdutoServiceImpl

/**
 * Localizador simples de dependências (sem Hilt/Dagger), único para os dois bancos.
 * [init] é chamado uma vez em [EasyShopApp.onCreate], depois de [ObjectBox.init].
 */
object ServiceLocator {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /** Produtos: banco de documentos (NoSQL, ObjectBox). */
    val produtoService: ProdutoService by lazy {
        ProdutoServiceImpl(ProdutoRepository(ObjectBox.store.boxFor(Produto::class.java)))
    }

    /** Clientes: banco relacional (SQL, Room). */
    val clienteService: ClienteService by lazy {
        ClienteServiceImpl(ClienteRepository(EasyShopDatabase.getInstance(appContext).clienteDao()))
    }
}
