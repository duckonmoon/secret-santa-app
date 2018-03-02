package com.softserveinc.test.secretsanta.application

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.entity.MessageHolder
import com.softserveinc.test.secretsanta.module.AppModule
import com.softserveinc.test.secretsanta.module.FirebaseModule
import com.softserveinc.test.secretsanta.service.FirebaseNotificationService
import com.softserveinc.test.secretsanta.service.MessagesService
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject


class App : Application() {
    companion object {
        lateinit var INSTANCE: App
    }

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var messagesService: MessagesService

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

        Thread{
            val message = MessageHolder.MessageBuilder()
                    .setTitle("wow")
                    .setBody("wow")
                    .setTopic("-L5nFvUVDPD6hc-35MJW")
                    .build()
            val response = messagesService.sendNotification("application/json",Constants.ID_MESSAGE,
                    message
                    )
                    .execute()
            Log.e("wow",response.errorBody()!!.charStream()!!.readText())
        }.start()
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