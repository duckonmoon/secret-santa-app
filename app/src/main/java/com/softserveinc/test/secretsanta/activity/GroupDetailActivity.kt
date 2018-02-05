package com.softserveinc.test.secretsanta.activity

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.HumanListAdapter
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.entity.GroupFull
import com.softserveinc.test.secretsanta.entity.Human
import com.softserveinc.test.secretsanta.entity.Member
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.viewmodel.HumanViewModel
import kotlinx.android.synthetic.main.activity_group_detail.*
import kotlinx.android.synthetic.main.group_details_tool_bar.*
import java.util.*
import javax.inject.Inject

class GroupDetailActivity : BaseActivity() {

    companion object {
        const val GROUP = "GROUP"
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    private lateinit var full: GroupFull

    private val group: Group by lazy {
        intent.extras[GROUP] as Group
    }

    private val viewModel: HumanViewModel by lazy {
        ViewModelProviders.of(this).get(HumanViewModel::class.java)
    }

    private val listener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            full = dataSnapshot.getValue(GroupFull::class.java)!!
            viewModel.group = full

            /*val u = ArrayList<Human>()
            u.addAll(viewModel.group!!.humans.values)
            Collections.shuffle(u)*/
            try {
                setAdapter()
            } catch (e: Exception) {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
        setContentView(R.layout.activity_group_detail)

        setSupportActionBar(tool_bar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = ""

        group_title_text_view.text = group.title
        member_count.text = getString(R.string.members_count,group.members)

        recyclerview.layoutManager = LinearLayoutManager(this)


        setInvitationButton()

        if (viewModel.group == null) {
            firebaseService.getGroupInfo(group.id, listener)
        } else {
            setAdapter()
        }
    }

    private fun setInvitationButton() {
        if (!group.activated) {
            activate_button.visibility = View.VISIBLE
            activate_button.setOnClickListener {
                group.activated = !group.activated
                firebaseService.updateGroupActivationStatus(group)
                activate_button.text = if (group.activated) {
                    getString(R.string.cancel_invitation)
                } else {
                    getString(R.string.accept_invitation)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_details_menu, menu)
        return true
    }


    private fun setAdapter() {
        recyclerview.adapter = HumanListAdapter(viewModel.group!!.humans.values.toList(), firebaseService.getUserNickname()!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
