package com.softserveinc.test.secretsanta.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.MyWishListAdapter
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.entity.GroupFull
import com.softserveinc.test.secretsanta.entity.Human
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_wish_list.*
import kotlinx.android.synthetic.main.group_details_tool_bar.*

class WishListActivity : BaseActivity() {

    companion object {
        const val FULL_GROUP = "FULL_GROUP"
        const val HUMAN = "HUMAN"
    }

    private val human: Human by lazy {
        intent.extras[HUMAN] as Human
    }

    private val group: GroupFull by lazy {
        intent.extras[FULL_GROUP] as GroupFull
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)
        setActionBar()
        setText()
        setRecyclerViewConfiguration()
        setAloneView()
        setGroupImage()
    }

    private fun setAloneView() {
        alone_view.visibility = if (human.preferences.size > 0) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun setGroupImage() {
        try {
            Picasso.with(this)
                    .load(Constants.images[human.image.toInt()]!!)
                    .noFade()
                    .into(group_image)
        } catch (e: Exception) {
        }
    }

    private fun setActionBar() {
        setSupportActionBar(tool_bar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = ""
    }

    private fun setText() {
        group_title_text_view.text = getString(R.string.his_wishlist, human.nickname)
        member_count.text = group.title
    }

    private fun setRecyclerViewConfiguration() {
        recyclerview.adapter = MyWishListAdapter(human.preferences)
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
