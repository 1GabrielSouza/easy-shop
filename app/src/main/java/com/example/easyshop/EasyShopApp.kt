package com.example.easyshop

import android.app.Application
import com.example.easyshop.data.nosql.ObjectBox


class EasyShopApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}
