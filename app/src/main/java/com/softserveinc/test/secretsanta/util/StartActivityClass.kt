package com.softserveinc.test.secretsanta.util

import android.app.Activity
import android.content.Intent
import com.softserveinc.test.secretsanta.activity.GroupsActivity
import com.softserveinc.test.secretsanta.activity.LoginActivity
import com.softserveinc.test.secretsanta.controller.MainController

class StartActivityClass {
    companion object {
        fun startGroupsActivity(activity: Activity) {
            val intent = Intent(activity, GroupsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
            activity.finish()
        }

        fun signOut(activity: Activity) {
            val intent = Intent(activity.applicationContext, LoginActivity::class.java)
            MainController.INSTANCE.signOut()
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity.startActivity(intent)
        }
    }
}