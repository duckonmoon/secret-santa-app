package com.softserveinc.test.secretsanta.util

import android.app.Activity
import android.content.Intent
import com.softserveinc.test.secretsanta.activity.GroupsActivity

class StartActivityClass {
    companion object {
        fun startGroupsActivity(activity: Activity) {
            val intent = Intent(activity, GroupsActivity::class.java)
            activity.startActivity(intent)
        }
    }
}