package com.softserveinc.test.secretsanta.application

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.module.AppModule
import com.softserveinc.test.secretsanta.module.FirebaseModule
import com.softserveinc.test.secretsanta.service.FirebaseService
import javax.inject.Inject


class App : Application() {
    companion object {
        lateinit var INSTANCE: App
    }

    val nicknames = ArrayList<String>()

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var firebaseService: FirebaseService

    val component: AuthComponent by lazy {
        DaggerAuthComponent
                .builder()
                .appModule(AppModule())
                .firebaseModule(FirebaseModule())
                .build()
    }


    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        INSTANCE = this
    }

    fun signOut() {
        auth.signOut()
    }
}