package com.softserveinc.test.secretsanta.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.*
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.entity.Member
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.viewmodel.MembersViewModel
import kotlinx.android.synthetic.main.create_group_activity.*
import javax.inject.Inject

class CreateGroupActivity : AppCompatActivity() {


    private val viewModel: MembersViewModel by lazy {
        ViewModelProviders.of(this).get(MembersViewModel::class.java)
    }

    private val members: ArrayList<Member> by lazy {
        viewModel.members
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_group_activity)
        App.INSTANCE.component.inject(this)

        members.add(firebaseService.getCurrentUserAsMember())

        val adapter = MemberListWrapper(MemberListAdapter(members, listener = object : OnItemClickListener {
            override fun onRemoveButtonClick(position: Int) {
                members.removeAt(position)
                recyclerview.adapter.notifyItemRemoved(position)
            }
        }), object : OnFooterActionDone {
            override fun onAddButtonClick(nickname: String, wrapperViewHolder: WrapperViewHolder) {
                if (!checkIfNickIsInMembers(nickname)) {
                    addIfExistsInCloud(nickname, wrapperViewHolder)
                } else {
                    wrapperViewHolder.setState(State.ADD_MEMBERS)
                    Toast.makeText(applicationContext, "Member already added", Toast.LENGTH_SHORT)
                            .show()
                }
            }

            override fun onDataRequested(nickname: String, wrapperViewHolder: WrapperViewHolder) {
                firebaseService.getAllNicknames(wrapperViewHolder, nickname)
            }
        })
        val layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = layoutManager
    }

    private fun addIfExistsInCloud(nickname: String, wrapperViewHolder: WrapperViewHolder) {
        firebaseService.checkIfNickExists(nickname = nickname, listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    members.add(Member(nickname, "1"))
                    recyclerview.adapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    Toast.makeText(applicationContext, "Pls check if nick is correct", Toast.LENGTH_SHORT)
                            .show()
                }
                wrapperViewHolder.setState(State.ADD_MEMBERS)
            }

        })
    }

    private fun checkIfNickIsInMembers(nickname: String): Boolean {
        return members.any { it.name == nickname }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_group_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {
                val groupTitle = group_title.text.toString()
                firebaseService.createNewGroup(members, groupTitle)

                StartActivityClass.finishActivityWithResultOk(this)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}