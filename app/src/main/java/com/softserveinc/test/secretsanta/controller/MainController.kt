package com.softserveinc.test.secretsanta.controller

import android.app.Application
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.module.AppModule


class MainController : Application() {
    companion object {
        lateinit var INSTANCE: MainController
    }


    private val component: AuthComponent by lazy {
        DaggerAuthComponent
                .builder()
                .appModule(AppModule())
                .build()
    }


    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        INSTANCE = this
    }
}