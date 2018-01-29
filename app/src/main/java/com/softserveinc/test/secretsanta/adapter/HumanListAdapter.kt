package com.softserveinc.test.secretsanta.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.softserveinc.test.secretsanta.entity.Human

class HumanListAdapter(private val humans : List<Human>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return null
    }

    override fun getItemCount(): Int = humans.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

    }

}