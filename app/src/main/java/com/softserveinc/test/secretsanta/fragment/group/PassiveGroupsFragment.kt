package com.softserveinc.test.secretsanta.fragment.group

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.SimpleGroupAdapter
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.Mapper
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.viewmodel.GroupViewModel
import kotlinx.android.synthetic.main.fragment_passive_groups.view.*
import javax.inject.Inject

class PassiveGroupsFragment : Fragment() {

    @Inject
    lateinit var firebaseService: FirebaseService

    lateinit var mView: View

    private val viewModel: GroupViewModel by lazy {
        ViewModelProviders.of(this).get(GroupViewModel::class.java)
    }

    private val onItemIterationListener = object : SimpleGroupAdapter.OnItemIterationListener {
        override fun onConfirmButtonClick(group: Group) {
            firebaseService.updateGroupActivationStatus(group = group)
        }

        override fun onItemClick(group: Group) {
            StartActivityClass.startGroupDetailActivity(activity!!, group)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_passive_groups, container, false)
        mView.setOnClickListener { }

        mView.recycler_view.adapter = SimpleGroupAdapter(viewModel.groups, onItemIterationListener)
        mView.recycler_view.layoutManager = LinearLayoutManager(context)

        if (viewModel.groups.isEmpty()) {
            mView.spinner.visibility = View.VISIBLE
        } else {
            mView.spinner.visibility = View.GONE
        }

        return mView
    }


    private fun getUpdate() {
        firebaseService.getAllNotActivatedGroups(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                viewModel.groups = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot)
                try {
                    mView.spinner.visibility = View.GONE

                    mView.recycler_view.adapter = SimpleGroupAdapter(viewModel.groups, onItemIterationListener)
                } catch (e: Exception) {
                }

            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    override fun onResume() {
        super.onResume()
        getUpdate()
    }
}
