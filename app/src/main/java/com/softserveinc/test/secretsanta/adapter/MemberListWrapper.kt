package com.softserveinc.test.secretsanta.adapter

import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.softserveinc.test.secretsanta.R

class MemberListWrapper(private val adapter: MemberListAdapter, private val listener: OnFooterActionDone)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CELL = 1
        private const val VIEW_TYPE_FOOTER = 0
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CELL) {
            return adapter.onCreateViewHolder(parent, viewType)
        }
        return WrapperViewHolder(view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.member_footer, parent, false), listener = listener)
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

class WrapperViewHolder(private val view: View?, private val listener: OnFooterActionDone)
    : RecyclerView.ViewHolder(view), ValueEventListener {
    private val label = view!!.findViewById<TextView>(R.id.label_with_add)!!
    private val layout = view!!.findViewById<LinearLayout>(R.id.additional_items)!!
    private val autoCompleteNicks = view!!.findViewById<AutoCompleteTextView>(R.id.autocomplete_nicknames)!!
    private val addButton = view!!.findViewById<Button>(R.id.add_member_button)!!
    private val loadingProgressBar = view!!.findViewById<ProgressBar>(R.id.loading_bar)!!

    private var array = arrayListOf<String>()
    private var state = State.ONLY_ADD_NEW_MEMBERS

    init {
        setNewAdapter()

        label.setOnClickListener {
            setState(State.ADD_MEMBERS)
        }


        addButton.setOnClickListener {
            setState(State.LOADING)
            listener.onAddButtonClick(autoCompleteNicks.text.toString().trim(), this)

        }

        autoCompleteNicks.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                listener.onDataRequested(s.toString(), this@WrapperViewHolder)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    private fun setNewArrayList(arrayList: ArrayList<String>) {
        array = arrayList
        setNewAdapter()
    }

    private fun setNewAdapter() {
        val adapter = ArrayAdapter<String>(view!!.context, android.R.layout.simple_list_item_1, array)
        autoCompleteNicks.setAdapter(adapter)
    }

    fun setState(state: State) {
        this.state = state

        Handler(Looper.getMainLooper()).post({
            when (state) {
                State.ONLY_ADD_NEW_MEMBERS -> {
                    loadingProgressBar.visibility = View.GONE
                    layout.visibility = View.GONE
                    label.visibility = View.VISIBLE
                }
                State.ADD_MEMBERS -> {
                    loadingProgressBar.visibility = View.GONE
                    layout.visibility = View.VISIBLE
                    label.visibility = View.GONE
                }
                State.LOADING -> {
                    loadingProgressBar.visibility = View.VISIBLE
                    layout.visibility = View.GONE
                    label.visibility = View.GONE
                }
            }
        })
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val arrayList = ArrayList<String>()
        dataSnapshot.children.mapTo(arrayList) { it.key }
        try {
            Handler(Looper.getMainLooper()).post({
                setNewArrayList(arrayList)
            })
        } catch (e: java.lang.Exception) {
        }
    }

    override fun onCancelled(p0: DatabaseError?) {}

}

enum class State {
    ONLY_ADD_NEW_MEMBERS,
    ADD_MEMBERS,
    LOADING
}


interface OnFooterActionDone {
    fun onAddButtonClick(nickname: String, wrapperViewHolder: WrapperViewHolder)
    fun onDataRequested(nickname: String, wrapperViewHolder: WrapperViewHolder)
}