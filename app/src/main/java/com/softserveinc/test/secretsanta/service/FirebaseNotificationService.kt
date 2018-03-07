package com.softserveinc.test.secretsanta.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.util.Mapper
import javax.inject.Inject

class FirebaseNotificationService : Service() {

    private val firebaseNotificationServiceName = "FirebaseNS"

    @Inject
    lateinit var firebaseService: FirebaseService

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        App.INSTANCE.component.inject(this)
        firebaseService.subscribeToGroupUpdates(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val groupsSize = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot).size
                val groupsSizeDevice = getGroupsSize()
                if (groupsSize > groupsSizeDevice) {
                    buildNotification()
                }
                saveNewNumberToPreferences(groupsSize)
            }
        })
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun saveNewNumberToPreferences(groupSize: Int) {
        sharedPreferences.edit().putInt(firebaseNotificationServiceName, groupSize).apply()
    }

    private fun getGroupsSize(): Int {
        return sharedPreferences.getInt(firebaseNotificationServiceName, Int.MAX_VALUE)
    }

    private fun buildNotification() {

        val mBuilder = NotificationCompat.Builder(applicationContext, "Hi")
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_main_icon_round)
                .setContentText(getString(R.string.new_groups_available))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(buildPendingIntent())
                .setVibrate(longArrayOf(0, 250, 200, 250))
                .setAutoCancel(true)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(0, mBuilder.build())
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }
}