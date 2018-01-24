package com.softserveinc.test.secretsanta.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.entity.Member


class MemberListAdapter(private var members : ArrayList<Member>,private val listener : OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.member_item, parent, false),listener = listener)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int){
        val member = members[position]
        holder!!.memberNameView.text = member.name
        if (position == 0){
            holder.memberCloseView.visibility = View.GONE
        }
    }



}

class ViewHolder(view: View,listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    val memberNameView : TextView = view.findViewById(R.id.member_text_view)
    val memberImageView : ImageView = view.findViewById(R.id.member_image_view)
    val memberCloseView : ImageView = view.findViewById(R.id.member_close)

    init {
        memberCloseView.setOnClickListener {
            listener.onRemoveButtonClick(adapterPosition)
        }
    }

}

interface OnItemClickListener{
    fun onRemoveButtonClick(position: Int)
}