package com.softserveinc.test.secretsanta.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.controller.MainController
import com.softserveinc.test.secretsanta.entity.Group

class SimpleGroupAdapter(private var groups: ArrayList<Group>,private val onItemIterationListener: OnItemIterationListener?)
    : RecyclerView.Adapter<SimpleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.group_item, parent, false),
                onButtonClickListener = object : SimpleViewHolder.OnButtonClickListener {
                    override fun onClick(position: Int): Boolean {
                        val group = groups[position]
                        group.activated = !group.activated
                        onItemIterationListener!!.onConfirmButtonClick(group)
                        return group.activated
                    }
                })
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val group = groups[position]
        holder.groupNameView.text = group.title
        holder.groupDescriptionView.text = MainController
                .INSTANCE
                .getString(R.string.members_count_and_date_created, group.members, group.date_created)
        if (!group.activated) {
            holder.groupConfirmationButton.visibility = View.VISIBLE
        }
    }

    interface OnItemIterationListener{
        fun onConfirmButtonClick(group: Group)
    }
}

class SimpleViewHolder(view: View, onButtonClickListener: OnButtonClickListener) : RecyclerView.ViewHolder(view) {
    val groupNameView: TextView = view.findViewById(R.id.group_name)
    val groupDescriptionView: TextView = view.findViewById(R.id.group_description)
    val groupConfirmationButton: Button = view.findViewById(R.id.confirm_button)

    init {
        groupConfirmationButton.setOnClickListener {
            if (!onButtonClickListener.onClick(adapterPosition)) {
                groupConfirmationButton.setText(R.string.confirm)
            } else {
                groupConfirmationButton.setText(R.string.cancel_confirmation)
            }
        }
    }

    interface OnButtonClickListener {
        fun onClick(position: Int): Boolean
    }
}
