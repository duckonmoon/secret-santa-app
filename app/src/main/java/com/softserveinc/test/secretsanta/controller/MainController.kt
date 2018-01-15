package com.softserveinc.test.secretsanta.controller

import android.app.Application
import com.beardedhen.androidbootstrap.TypefaceProvider

class MainController : Application() {
    companion object {
        lateinit var INSTANCE: MainController
    }

    override fun onCreate() {
        super.onCreate()
        TypefaceProvider.registerDefaultIconSets()
        INSTANCE = this
    }
}