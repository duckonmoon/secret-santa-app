package com.softserveinc.test.secretsanta.activity

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.HumanListAdapter
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.dialog.NewYearConfirmationDialog
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.entity.GroupFull
import com.softserveinc.test.secretsanta.entity.Human
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.view.SantaToast
import com.softserveinc.test.secretsanta.viewmodel.HumanViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_group_detail.*
import kotlinx.android.synthetic.main.group_details_tool_bar.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GroupDetailActivity : BaseActivity() {

    companion object {
        const val GROUP = "GROUP"
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    private lateinit var menu: Menu

    private val group: Group by lazy {
        if (viewModel.group == null) {
            viewModel.group = intent.extras[GROUP] as Group
            viewModel.group!!
        } else {
            viewModel.group!!
        }

    }

    private val humanListener = object : HumanListAdapter.OnHumanItemClickListener {
        override fun onClick(human: Human) {
            if (human.nickname == firebaseService.getUserNickname()) {
                StartActivityClass.startMyWishListActivity(this@GroupDetailActivity, viewModel.groupFull!!)
                return
            }
            StartActivityClass.startWishListActivity(
                    activity = this@GroupDetailActivity,
                    human = human,
                    group = viewModel.groupFull!!)
        }
    }

    private val viewModel: HumanViewModel by lazy {
        ViewModelProviders.of(this).get(HumanViewModel::class.java)
    }

    private val listener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val full = dataSnapshot.getValue(GroupFull::class.java)!!
            viewModel.groupFull = full
            try {
                setAdapter()
                setMenuItemVisibility()
            } catch (e: Exception) {
            }
        }
    }

    private fun setMenuItemVisibility() {
        if (viewModel.groupFull != null) {
            menu.findItem(R.id.randomize).isVisible = checkRights()
            menu.findItem(R.id.wish_list).isVisible = true
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

        setGroupImage()

        group_title_text_view.text = group.title
        member_count.text = getString(R.string.members_count, group.members)

        recyclerview.layoutManager = LinearLayoutManager(this)

        setInvitationButton()

        if (viewModel.groupFull == null) {
            firebaseService.getGroupInfo(group.id, listener)
        } else {
            setAdapter()
        }
    }

    private fun setGroupImage() {
        try {
            Picasso.with(this)
                    .load(Constants.images[group.imageCode]!!)
                    .noFade()
                    .into(group_image)
        } catch (e: Exception) {
        }
    }

    private fun setInvitationButton() {
        if (group.activated == Group.PASSIVE || group.activated == Group.DELETED) {
            activate_button.visibility = View.VISIBLE
            activate_button.setText(if (group.activated == Group.PASSIVE) {
                R.string.accept_invitation
            } else {
                R.string.restore_group
            })
            activate_button.setOnClickListener {
                group.activated = Group.ACTIVATED
                firebaseService.updateGroupActivationStatus(group)
                activate_button.visibility = View.GONE


                SantaToast.makeText(this, if (group.activated == Group.PASSIVE) {
                    getString(R.string.invitation_accepted, group.title)
                } else {
                    getString(R.string.group_restored, group.title)
                },
                        SantaToast.LENGTH_LONG, SantaToast.SUCCESS, R.drawable.christmas_house, R.drawable.santaa).show()
            }
        }
    }

    private fun checkRights(): Boolean {
        if (viewModel.groupFull != null) {
            val nickname: String = firebaseService.getUserNickname()!!
            return viewModel.groupFull!!.humans[nickname]!!.admin
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.group_details_menu, menu)
        this.menu = menu
        setMenuItemVisibility()
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.randomize -> randomize()
            R.id.wish_list -> StartActivityClass.startMyWishListActivity(this, viewModel.groupFull!!)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun randomize() {
        if (!viewModel.groupFull!!.randomize) {
            doRandomChanges()
        } else {
            buildConfirmationDialog()
        }
    }

    private fun doRandomChanges() {
        orderHumansInRandomWay()
        notifyFirebase()
        recyclerview.adapter.notifyDataSetChanged()
    }

    private fun notifyFirebase() {
        firebaseService.randomizeGroup(viewModel.groupFull!!)
    }

    private fun buildConfirmationDialog() {
        NewYearConfirmationDialog.Builder(this)
                .setMessage(getString(R.string.rerandom))
                .setYesButtonClickListener(View.OnClickListener {
                    doRandomChanges()
                })
                .setNoButtonClickListener(View.OnClickListener {

                }).build().show()
    }

    private fun orderHumansInRandomWay(): ArrayList<Human> {
        val humans = ArrayList<Human>()
        humans.addAll(viewModel.groupFull!!.humans.values)
        Collections.shuffle(humans)
        for (i in 0 until humans.size - 1) {
            humans[i].giftedBy = humans[i + 1].nickname
        }
        humans[humans.size - 1].giftedBy = humans[0].nickname
        return humans
    }


    private fun setAdapter() {
        progress_bar.visibility = View.GONE

        recyclerview.adapter = HumanListAdapter(viewModel.groupFull!!.humans.values.toList(),
                firebaseService.getUserNickname()!!,
                humanListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            viewModel.groupFull = data.extras[MyWishListActivity.FULL_GROUP] as GroupFull
        }
    }
}
