package com.example.kachow_cursach

import android.app.Application
import com.example.kachow_cursach.di.AppModule

class KaChowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}