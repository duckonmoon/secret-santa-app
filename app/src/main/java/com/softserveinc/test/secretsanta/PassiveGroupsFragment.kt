package com.softserveinc.test.secretsanta


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
import com.softserveinc.test.secretsanta.adapter.SimpleGroupAdapter
import com.softserveinc.test.secretsanta.controller.MainController
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.Mapper
import com.softserveinc.test.secretsanta.viewmodel.StringsViewModel
import kotlinx.android.synthetic.main.fragment_passive_groups.view.*
import javax.inject.Inject

class PassiveGroupsFragment : Fragment() {

    @Inject
    lateinit var firebaseService: FirebaseService

    lateinit var mView: View

    private val viewModel : StringsViewModel by lazy {
        ViewModelProviders.of(this).get(StringsViewModel::class.java)
    }

    private val onItemIterationListener = object : SimpleGroupAdapter.OnItemIterationListener{
        override fun onConfirmButtonClick(group: Group) {
            firebaseService.updateGroupActivationStatus(group = group)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainController.INSTANCE.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_passive_groups, container, false)
        mView.setOnClickListener {  }

        mView.recycler_view.adapter = SimpleGroupAdapter(viewModel.groups,onItemIterationListener)
        mView.recycler_view.layoutManager = LinearLayoutManager(context)

        if (viewModel.groups.isEmpty()) {
            getUpdate()
        } else {
            mView.spinner.visibility = View.GONE
        }

        return mView
    }


    private fun getUpdate() {
        mView.spinner.visibility = View.VISIBLE

        firebaseService.getAllNotActivatedGroups(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mView.spinner.visibility = View.GONE
                viewModel.groups = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot)
                mView.recycler_view.adapter = SimpleGroupAdapter(viewModel.groups,onItemIterationListener)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }
}
