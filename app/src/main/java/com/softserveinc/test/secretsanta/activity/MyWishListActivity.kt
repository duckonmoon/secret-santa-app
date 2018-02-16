package com.softserveinc.test.secretsanta.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.MyWishListAdapter
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.dialog.NewYearConfirmationDialog
import com.softserveinc.test.secretsanta.entity.GroupFull
import com.softserveinc.test.secretsanta.entity.Human
import com.softserveinc.test.secretsanta.service.FirebaseService
import com.softserveinc.test.secretsanta.util.StartActivityClass
import com.softserveinc.test.secretsanta.viewmodel.WishesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_wish_list.*
import kotlinx.android.synthetic.main.group_details_tool_bar.*
import java.util.*
import javax.inject.Inject


class MyWishListActivity : BaseActivity() {

    companion object {
        const val FULL_GROUP = "FULL_GROUP"
    }

    @Inject
    lateinit var firebaseService: FirebaseService

    private val viewModel: WishesViewModel by lazy {
        ViewModelProviders.of(this).get(WishesViewModel::class.java)
    }

    private val group: GroupFull by lazy {
        intent.extras[FULL_GROUP] as GroupFull
    }

    private val me: Human by lazy {
        group.humans[firebaseService.getUserNickname()]!!
    }

    private var mItemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition

                    Collections.swap(viewModel.wishes, fromPos, toPos)

                    recyclerView.adapter.notifyItemMoved(fromPos, toPos)

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.wishes.removeAt(viewHolder.adapterPosition)
                    recyclerview.adapter.notifyDataSetChanged()
                }
            })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.INSTANCE.component.inject(this)
        setContentView(R.layout.activity_my_wish_list)

        setViewModelItems(savedInstanceState)

        setActionBar()

        setTextAndListenersOnPage()

        setRecyclerViewConfiguration()

        setGroupImage()
    }

    private fun setGroupImage(){
        try {
            Picasso.with(this)
                    .load(Constants.images[me.image.toInt()]!!)
                    .noFade()
                    .into(group_image)
        } catch (e : Exception){
        }
    }

    private fun setViewModelItems(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.wishes.addAll(me.preferences)
            viewModel.reset()
        }
    }

    private fun setActionBar() {
        setSupportActionBar(tool_bar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = ""
    }


    private fun setTextAndListenersOnPage() {
        group_title_text_view.text = getString(R.string.my_wishlist)
        member_count.text = group.title

        add_button.setOnClickListener {
            viewModel.wishes.add(wish_text.text.toString().trim())
            recyclerview.adapter.notifyDataSetChanged()
        }
    }


    private fun setRecyclerViewConfiguration() {
        recyclerview.adapter = MyWishListAdapter(viewModel.wishes)
        recyclerview.layoutManager = LinearLayoutManager(this)
        mItemTouchHelper.attachToRecyclerView(recyclerview)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onBackPressed() {
        if (viewModel.isChanged) {
            NewYearConfirmationDialog.Builder(this)
                    .setMessage(getString(R.string.accept_leave))
                    .setYesButtonClickListener(View.OnClickListener { super.onBackPressed() })
                    .setNoButtonClickListener(View.OnClickListener { })
                    .build().show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_group_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {
                updateFirebase()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun updateFirebase() {
        if (viewModel.isChanged) {
            firebaseService.setMyPreferences(group, me, viewModel.wishes)
            StartActivityClass.finishActivityWithReturnFullGroup(this, group)
        } else {
            onBackPressed()
        }
    }
}
