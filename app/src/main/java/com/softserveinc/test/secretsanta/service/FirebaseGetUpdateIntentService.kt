package com.softserveinc.test.secretsanta.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.util.Mapper
import javax.inject.Inject

class FirebaseGetUpdateIntentService : IntentService(TAG) {
    var messaging: FirebaseMessaging? = null

    @Inject
    lateinit var auth : FirebaseAuth

    @Inject
    lateinit var service: FirebaseService

    override fun onCreate() {
        super.onCreate()
        App.INSTANCE.component.inject(this)
        messaging = FirebaseMessaging.getInstance()
    }


    companion object {
        const val TAG = "FirebaseGetUpdateServic"
        const val SUB = "SUB"
        const val UNSUB = "UNSUB"
    }

    override fun onHandleIntent(intent: Intent) {
        if (intent.extras[TAG] == SUB){
            subscribe()
        } else if(intent.extras[TAG] == UNSUB) {
            unsubscribe()
        }
    }

    private fun unsubscribe() {
        service.getAllActivatedGroups(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val groups = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot)
                for (group in groups) {
                    messaging!!.unsubscribeFromTopic(group.id)
                }
            }

        })
    }

    private fun subscribe(){
        service.getAllActivatedGroups(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val groups = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot)
                for (group in groups) {
                    messaging!!.subscribeToTopic(group.id)
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"Destroyed")
    }
}