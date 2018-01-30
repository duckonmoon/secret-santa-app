package com.softserveinc.test.secretsanta.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.databinding.ActivityGroupDetailBinding
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.entity.GroupFull
import com.softserveinc.test.secretsanta.service.FirebaseService
import javax.inject.Inject

class GroupDetailActivity : AppCompatActivity() {

    companion object {
        const val GROUP = "GROUP"
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    private lateinit var full: GroupFull

    private val group: Group by lazy {
        intent.extras[GROUP] as Group
    }

    private val listener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            full = dataSnapshot.getValue(GroupFull::class.java)!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)

        bindData()

        firebaseService.getGroupInfo(group.id, listener)
    }

    private fun bindData() {
        val binding = DataBindingUtil.setContentView<ActivityGroupDetailBinding>(this, R.layout.activity_group_detail)
        binding.group = group
        binding.executePendingBindings()
    }
}
