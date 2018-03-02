package com.softserveinc.test.secretsanta.service

import android.app.Notification
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softserveinc.test.secretsanta.R


open class FirebaseMessaginService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from!!)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

        }

        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
            val mBuilder = NotificationCompat.Builder(applicationContext, "Hi")
                    .setContentTitle(remoteMessage.notification!!.body)
                    .setSmallIcon(R.mipmap.ic_main_icon_round)
                    .setContentText(getString(R.string.new_groups_available))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(0, mBuilder.build())
        }
    }
}