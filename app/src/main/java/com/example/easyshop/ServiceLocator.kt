package com.example.easyshop

import com.example.easyshop.data.nosql.ObjectBox
import com.example.easyshop.data.nosql.Produto
import com.example.easyshop.data.nosql.ProdutoRepository
import com.example.easyshop.service.ProdutoService
import com.example.easyshop.service.ProdutoServiceImpl

/** Localizador simples de dependências (sem Hilt/Dagger). Requer [ObjectBox.init] já chamado. */
object ServiceLocator {
    /** Service real de produtos (ObjectBox). A UI deve usar este no lugar do fake. */
    val produtoService: ProdutoService by lazy {
        ProdutoServiceImpl(ProdutoRepository(ObjectBox.store.boxFor(Produto::class.java)))
    }
}
