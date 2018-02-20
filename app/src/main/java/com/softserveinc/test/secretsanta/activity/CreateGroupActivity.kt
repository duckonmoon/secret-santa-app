package com.softserveinc.test.secretsanta.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.*
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.dialog.ChangeImageDialog
import com.softserveinc.test.secretsanta.entity.Member
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.viewmodel.MembersViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.create_group_activity.*
import javax.inject.Inject

class CreateGroupActivity : BaseActivity() {

    private val viewModel: MembersViewModel by lazy {
        ViewModelProviders.of(this).get(MembersViewModel::class.java)
    }

    private val members: ArrayList<Member> by lazy {
        viewModel.members
    }

    private val listener = object : ChangeImageDialog.DoneListener {
        override fun onDone(currentImageContentDescription: Int) {
            viewModel.currentImage = currentImageContentDescription

            try {
                Picasso.with(this@CreateGroupActivity)
                        .load(Constants.images[viewModel.currentImage]!!)
                        .noFade()
                        .into(group_image)
            } catch (e: Exception) {
            }
        }

    }

    @Inject
    lateinit var firebaseService: FirebaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_group_activity)
        App.INSTANCE.component.inject(this)

        setSupportActionBar(tool_bar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (savedInstanceState == null) {
            members.add(firebaseService.getCurrentUserAsMember())
        }

        setRecyclerView()

        loadImageAndSetListener()


    }

    private fun loadImageAndSetListener() {
        group_image.setOnClickListener {
            ChangeImageDialog(this, viewModel.currentImage, listener).show()
        }

        try {
            Picasso.with(this@CreateGroupActivity)
                    .load(Constants.images[viewModel.currentImage]!!)
                    .noFade()
                    .into(group_image)
        } catch (e: Exception) {
        }
    }

    private fun addIfExistsInCloud(nickname: String, wrapperViewHolder: WrapperViewHolder) {
        firebaseService.checkIfNickExists(nickname = nickname, listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.value != null) {
                        members.add(Member(nickname, dataSnapshot.value!!.toString()))
                        recyclerview.adapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT)
                                .show()
                        wrapperViewHolder.setState(State.ADD_MEMBERS_ADDED)
                    } else {
                        Toast.makeText(applicationContext, "Pls check if nick is correct", Toast.LENGTH_SHORT)
                                .show()
                        wrapperViewHolder.setState(State.ADD_MEMBERS_NOT_ADDED)
                    }

                } catch (e: Exception) {
                }
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
                firebaseService.createNewGroup(members, groupTitle, viewModel.currentImage)

                StartActivityClass.finishActivityWithResultOk(this)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setRecyclerView() {
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
                    wrapperViewHolder.setState(State.ADD_MEMBERS_NOT_ADDED)
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}