package com.softserveinc.test.secretsanta.fragment.group

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.SimpleGroupAdapter
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.dialog.NewYearConfirmationDialog
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.Mapper
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.view.SantaToast
import com.softserveinc.test.secretsanta.viewmodel.GroupViewModel
import kotlinx.android.synthetic.main.fragment_passive_groups.view.*
import javax.inject.Inject

class DeletedGroupsFragment : Fragment() {

    @Inject
    lateinit var firebaseService: FirebaseService

    lateinit var mView: View

    private val viewModel: GroupViewModel by lazy {
        ViewModelProviders.of(this).get(GroupViewModel::class.java)
    }

    private val onItemIterationListener = object : SimpleGroupAdapter.OnItemIterationListener {
        override fun onConfirmButtonClick(group: Group) {
        }

        override fun onItemClick(group: Group) {
            StartActivityClass.startGroupDetailActivity(activity!!, group)
        }
    }

    private var mItemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    try {
                        buildNewYearDialog(viewHolder)
                    } catch (e: Exception) {
                    }
                }
            })

    private fun buildNewYearDialog(viewHolder: RecyclerView.ViewHolder) {
        NewYearConfirmationDialog.Builder(activity!!)
                .setMessage(getString(R.string.delete_group,
                        viewModel.groups[viewHolder.adapterPosition].title))
                .setYesButtonClickListener(View.OnClickListener {
                    firebaseService.deleteGroup(viewModel.groups[viewHolder.adapterPosition])
                    SantaToast.makeText(context!!, getString(R.string.group_completely_deleted, viewModel.groups[viewHolder.adapterPosition].title),
                            SantaToast.LENGTH_LONG, SantaToast.DEFAULT, R.drawable.christmas_house, null).show()
                    viewModel.groups.removeAt(viewHolder.adapterPosition)
                    mView.recycler_view.adapter.notifyDataSetChanged()
                    checkIfGroupsExists()
                })
                .setNoButtonClickListener(View.OnClickListener {
                    mView.recycler_view.adapter.notifyDataSetChanged()
                })
                .build().show()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_passive_groups, container, false)

        mView.setOnClickListener { }

        mView.recycler_view.adapter = SimpleGroupAdapter(viewModel.groups, onItemIterationListener)
        mView.recycler_view.layoutManager = LinearLayoutManager(context)
        mItemTouchHelper.attachToRecyclerView(mView.recycler_view)

        if (viewModel.groups.isEmpty()) {
            mView.spinner.visibility = View.VISIBLE
        } else {
            mView.spinner.visibility = View.GONE
        }

        return mView
    }

    private fun getUpdate() {
        firebaseService.getTrash(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                viewModel.groups = Mapper.mapFromDataSnapshotGroupsToStringGroups(dataSnapshot)
                try {
                    mView.spinner.visibility = View.GONE
                    checkIfGroupsExists()
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

    private fun checkIfGroupsExists() {
        mView.alone_view.visibility = if (viewModel.groups.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}