package com.softserveinc.test.secretsanta.util

import android.app.Activity
import android.content.Intent
import com.softserveinc.test.secretsanta.activity.*
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.entity.GroupFull
import com.softserveinc.test.secretsanta.entity.Human

class StartActivityClass {
    companion object {
        const val REQUESTED_CODE_CREATE_GROUP_ACTIVITY = 1


        fun startGroupsActivity(activity: Activity) {
            val intent = Intent(activity, GroupsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
            activity.finish()
        }

        fun signOut(activity: Activity) {
            val intent = Intent(activity.applicationContext, LoginActivity::class.java)
            App.INSTANCE.signOut()
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity.startActivity(intent)
            activity.finish()
        }

        fun startCreateGroupsActivityForResult(activity: Activity) {
            activity.startActivityForResult(Intent(activity, CreateGroupActivity::class.java),
                    REQUESTED_CODE_CREATE_GROUP_ACTIVITY)
        }

        fun finishActivityWithResultOk(activity: Activity) {
            val intent = Intent()
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }

        fun startGroupDetailActivity(activity: Activity, group: Group) {
            val intent = Intent(activity, GroupDetailActivity::class.java)
            intent.putExtra(GroupDetailActivity.GROUP, group)
            activity.startActivity(intent)
        }

        fun startMyWishListActivity(activity: Activity, group: GroupFull) {
            val intent = Intent(activity, MyWishListActivity::class.java)
            intent.putExtra(MyWishListActivity.FULL_GROUP, group)
            activity.startActivityForResult(intent, Constants.REQUEST_CODE)
        }

        fun finishActivityWithReturnFullGroup(activity: Activity, group: GroupFull) {
            val intent = Intent()
            intent.putExtra(MyWishListActivity.FULL_GROUP, group)
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }

        fun startWishListActivity(activity: Activity, human: Human, group: GroupFull) {
            val intent = Intent(activity, WishListActivity::class.java)
            intent.putExtra(WishListActivity.HUMAN, human)
            intent.putExtra(WishListActivity.FULL_GROUP, group)
            activity.startActivity(intent)
        }

    }
}