package com.softserveinc.test.secretsanta.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.application.App


class MyWishListAdapter(private val wishes: MutableList<String>) : RecyclerView.Adapter<MyWishListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWishListViewHolder {
        return MyWishListViewHolder(view = LayoutInflater.from(parent.context)
                .inflate(R.layout.wish_item, parent, false))
    }

    override fun getItemCount(): Int {
        return wishes.size
    }

    override fun onBindViewHolder(holder: MyWishListViewHolder, position: Int) {
        holder.textField.text = App.INSTANCE.getString(R.string.wish_string, wishes[position])
    }
}


class MyWishListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textField: TextView = view.findViewById(R.id.text_field)
}