package com.softserveinc.test.secretsanta.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.adapter.MemberListAdapter
import com.softserveinc.test.secretsanta.adapter.MemberListWrapper
import com.softserveinc.test.secretsanta.adapter.OnAddMemberClickListener
import com.softserveinc.test.secretsanta.entity.Member
import kotlinx.android.synthetic.main.create_group_activity.*

class CreateGroupActivity : AppCompatActivity() {

    private val members = arrayListOf(
        Member("Item0","wow"),
        Member("Item1","wow"),
        Member("Item2","wow"),
        Member("Item3","wow"),
        Member("Item4","wow"),
        Member("Item5","wow"),
        Member("Item6","wow"),
        Member("Item7","wow"),
        Member("Item8","wow"),
        Member("Item9","wow")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_group_activity)

        val adapter = MemberListWrapper(MemberListAdapter(members),object : OnAddMemberClickListener {
            override fun onClick(nickname: String) {

            }

        })
        val layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = layoutManager
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_group_menu, menu)
        return true
    }
}