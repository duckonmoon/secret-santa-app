package com.softserveinc.test.secretsanta.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.SimpleGroupAdapter
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.dialog.NewYearConfirmationDialog
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.fragment.group.DeletedGroupsFragment
import com.softserveinc.test.secretsanta.fragment.group.PassiveGroupsFragment
import com.softserveinc.test.secretsanta.fragment.group.ProfileFragment
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.Mapper
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.view.SantaToast
import com.softserveinc.test.secretsanta.viewmodel.GroupViewModel
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.app_bar_groups.*
import kotlinx.android.synthetic.main.content_groups.*
import kotlinx.android.synthetic.main.nav_header_groups.view.*
import javax.inject.Inject

class GroupsActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: GroupViewModel by lazy {
        ViewModelProviders.of(this).get(GroupViewModel::class.java)
    }

    private var mItemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    buildNewYearDialog(viewHolder)
                }
            })

    private val listener = object : SimpleGroupAdapter.OnItemIterationListener {
        override fun onConfirmButtonClick(group: Group) {
        }

        override fun onItemClick(group: Group) {
            StartActivityClass.startGroupDetailActivity(activity = this@GroupsActivity, group = group)
        }
    }

    @Inject
    lateinit var firebaseService: FirebaseService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        setSupportActionBar(toolbar)
        Log.e("I am here",FirebaseInstanceId.getInstance().id)
        App.INSTANCE.component.inject(this)

        fab.setOnClickListener {
            addClick()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.getHeaderView(0).group_name.text = getString(R.string.ur_nickname_is, firebaseService.getUserNickname())
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.adapter = SimpleGroupAdapter(viewModel.groups, listener)
        recycler_view.layoutManager = LinearLayoutManager(this)
        mItemTouchHelper.attachToRecyclerView(recycler_view)

        if (viewModel.groups.isEmpty()) {
            getUpdate()
        } else {
            spinner.visibility = View.GONE
        }
    }

    private fun getUpdate() {
        spinner.visibility = View.VISIBLE

        firebaseService.getAllActivatedGroups(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                viewModel.groups = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot)
                try {
                    spinner.visibility = View.GONE
                    checkIfGroupsExists()
                    recycler_view.adapter = SimpleGroupAdapter(viewModel.groups, listener)
                } catch (e: Exception) {
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            supportFragmentManager.fragments.size > 0 -> {
                val transaction = supportFragmentManager.beginTransaction()
                for (fragment in supportFragmentManager.fragments) {
                    transaction.remove(fragment)
                }
                transaction.commit()
            }
            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.groups, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> StartActivityClass.signOut(this)
            R.id.action_add -> addClick()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun addClick() {
        StartActivityClass.startCreateGroupsActivityForResult(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.unchecked_groups -> transaction.replace(R.id.container, PassiveGroupsFragment())
            R.id.deleted_groups -> transaction.replace(R.id.container, DeletedGroupsFragment())
            R.id.profile -> transaction.replace(R.id.container, ProfileFragment())
            R.id.my_groups -> {
                getUpdate()
                for (fragment in supportFragmentManager.fragments) {
                    transaction.remove(fragment)
                }
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        transaction.commit()
        return true
    }

    private fun buildNewYearDialog(viewHolder: RecyclerView.ViewHolder) {
        NewYearConfirmationDialog.Builder(this)
                .setMessage(getString(R.string.confirm_move_group_to_trash,
                        viewModel.groups[viewHolder.adapterPosition].title))
                .setYesButtonClickListener(View.OnClickListener {
                    firebaseService.moveGroupToTrash(viewModel.groups[viewHolder.adapterPosition])
                    SantaToast.makeText(this, getString(R.string.group_moved_trash, viewModel.groups[viewHolder.adapterPosition].title),
                            SantaToast.LENGTH_LONG, SantaToast.INFO, R.drawable.christmas_house, null).show()
                    viewModel.groups.removeAt(viewHolder.adapterPosition)
                    recycler_view.adapter.notifyDataSetChanged()
                    checkIfGroupsExists()
                })
                .setNoButtonClickListener(View.OnClickListener {
                    recycler_view.adapter.notifyDataSetChanged()
                })
                .build().show()
    }

    private fun checkIfGroupsExists() {
        alone_view.visibility = if (viewModel.groups.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == StartActivityClass.REQUESTED_CODE_CREATE_GROUP_ACTIVITY && resultCode == RESULT_OK) {
            SantaToast.makeText(this, getString(R.string.group_created_successfully), SantaToast.LENGTH_LONG,
                    SantaToast.SUCCESS, R.drawable.christmas_house, R.drawable.face).show()
            getUpdate()
        }
    }
}
