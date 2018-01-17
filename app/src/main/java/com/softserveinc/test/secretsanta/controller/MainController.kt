package com.softserveinc.test.secretsanta.controller

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class MainController : Application() {
    companion object {
        lateinit var INSTANCE: MainController
    }

    lateinit var auth : FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        auth = FirebaseAuth.getInstance()

    }
}