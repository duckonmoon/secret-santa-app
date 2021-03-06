package com.softserveinc.test.secretsanta.application

import android.app.Application
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.module.AppModule
import com.softserveinc.test.secretsanta.module.FirebaseModule
import com.softserveinc.test.secretsanta.service.FirebaseNotificationService
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject


class App : Application() {
    companion object {
        lateinit var INSTANCE: App
    }

    @Inject
    lateinit var auth: FirebaseAuth

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

        try {
            auth.currentUser!!.reload()
        } catch (e: NullPointerException) {

        }

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    fun signOut() {
        Thread {
            FirebaseInstanceId.getInstance().deleteInstanceId()
        }.start()

        auth.signOut()
    }

    fun startFirebaseNotificationService() {
        startService(Intent(this, FirebaseNotificationService::class.java))
    }
}