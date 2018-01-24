package com.softserveinc.test.secretsanta.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.controller.MainController

class MemberListWrapper(private val adapter: MemberListAdapter,private val listener: OnAddMemberClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_CELL = 1
    private val VIEW_TYPE_FOOTER = 0


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CELL) {
            return adapter.onCreateViewHolder(parent, viewType)
        }
        return WrapperViewHolder(view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.member_footer, parent, false),listener = listener)
    }

    override fun getItemCount(): Int {
        return adapter.itemCount + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != adapter.itemCount) {
            VIEW_TYPE_CELL
        } else {
            VIEW_TYPE_FOOTER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position < adapter.itemCount) {
            adapter.onBindViewHolder(holder as ViewHolder, position)
        } else {

        }
    }
}

class WrapperViewHolder(view: View?,private val listener: OnAddMemberClickListener) : RecyclerView.ViewHolder(view) {
    val label = view!!.findViewById<TextView>(R.id.label_with_add)
    val autoCompleteNicks = view!!.findViewById<AutoCompleteTextView>(R.id.autocomplete_nicknames)
    val addButton = view!!.findViewById<Button>(R.id.add_member_button)

    init {
        val array = MainController.INSTANCE.nicknames

        val adapter = ArrayAdapter<String>(view!!.context, android.R.layout.simple_list_item_1, array)
        autoCompleteNicks.setAdapter(adapter)

        addButton.setOnClickListener{
            listener.onClick(autoCompleteNicks.text.toString())
        }
    }
}

interface OnAddMemberClickListener {
    fun onClick(nickname: String)
}