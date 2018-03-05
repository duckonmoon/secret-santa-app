package com.softserveinc.test.secretsanta.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.application.App
import javax.inject.Inject

open class FirebaseMessaginService : FirebaseMessagingService() {

    @Inject
    lateinit var firebaseService: FirebaseService

    private fun getNickname(): String = firebaseService.getUserNickname()!!


    override fun onCreate() {
        super.onCreate()
        App.INSTANCE.component.inject(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("sadf","I am here")
        var amIAdmin = false
        var amIEmpty = false

        if (remoteMessage.data.isNotEmpty()) {
            amIEmpty = remoteMessage.data["empty_members"].toString().contains('"' + getNickname() + '"')
            amIAdmin = remoteMessage.data["admin"].toString() == getNickname()
        }

        if (!amIAdmin && amIEmpty) {
            val mBuilder = NotificationCompat.Builder(applicationContext, "Hi")
                    .setContentTitle(remoteMessage.data["title"].toString())
                    .setSmallIcon(R.mipmap.ic_main_icon_round)
                    .setContentText(remoteMessage.data["body"].toString())
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(longArrayOf(0, 250, 200, 250))
                    .setContentIntent(buildPendingIntent())
                    .setAutoCancel(true)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(0, mBuilder.build())
        }
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


}