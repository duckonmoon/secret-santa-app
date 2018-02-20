package com.softserveinc.test.secretsanta.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.softserveinc.test.secretsanta.application.App

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        (context!!.applicationContext as App).startFirebaseNotificationService()
    }
}