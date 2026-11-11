package com.example.easyshop.data.nosql

import android.content.Context
import io.objectbox.BoxStore


object ObjectBox {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        if (!::store.isInitialized) {
            store = MyObjectBox.builder().androidContext(context.applicationContext).build()
        }
    }
}
