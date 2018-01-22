package com.softserveinc.test.secretsanta.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softserveinc.test.secretsanta.R

class MemberListWrapper(private val adapter: MemberListAdapter)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_CELL = 1
    private val VIEW_TYPE_FOOTER = 0



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CELL){
            return adapter.onCreateViewHolder(parent,viewType)
        }
        return WrapperViewHolder(view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.member_footer, parent, false))
    }

    override fun getItemCount(): Int {
        return adapter.itemCount+1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != adapter.itemCount){
            VIEW_TYPE_CELL
        } else {
            VIEW_TYPE_FOOTER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position < adapter.itemCount){
            adapter.onBindViewHolder(holder as ViewHolder,position)
        } else {

        }
    }
}

class WrapperViewHolder(view: View?) : RecyclerView.ViewHolder(view){

}