package com.softserveinc.test.secretsanta.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.SimpleGroupAdapter
import com.softserveinc.test.secretsanta.component.AuthComponent
import com.softserveinc.test.secretsanta.component.DaggerAuthComponent
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.Mapper
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.viewmodel.StringsViewModel
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.app_bar_groups.*
import kotlinx.android.synthetic.main.content_groups.*
import javax.inject.Inject

class GroupsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: StringsViewModel by lazy {
        ViewModelProviders.of(this).get(StringsViewModel::class.java)
    }

    private val component: AuthComponent by lazy {
        DaggerAuthComponent.builder().build()
    }

    @Inject
    lateinit var firebaseService: FirebaseService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        setSupportActionBar(toolbar)
        component.inject(this)

        fab.setOnClickListener {
            addClick()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.adapter = SimpleGroupAdapter(viewModel.groups)
        recycler_view.layoutManager = LinearLayoutManager(this)

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
                spinner.visibility = View.GONE
                viewModel.groups = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot)
                recycler_view.adapter = SimpleGroupAdapter(viewModel.groups)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
        when (item.itemId) {

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == StartActivityClass.REQUESTED_CODE_CREATE_GROUP_ACTIVITY && resultCode == RESULT_OK) {
            getUpdate()
        }
    }
}
