
package com.example.easyshop

import android.content.Context
import com.example.easyshop.business.ClienteService
import com.example.easyshop.data.local.EasyShopDatabase
import com.example.easyshop.data.repository.ClienteRepository

object ClienteServiceLocator {

    @Volatile
    private var instance: ClienteService? = null

    fun getClienteService(context: Context): ClienteService {
        return instance ?: synchronized(this) {
            instance ?: run {
                val database = EasyShopDatabase.getInstance(context)
                val repository = ClienteRepository(database.clienteDao())

                ClienteService(repository).also {
                    instance = it
                }
            }
        }
    }
}
