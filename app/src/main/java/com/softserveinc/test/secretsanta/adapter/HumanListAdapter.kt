package com.softserveinc.test.secretsanta.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.softserveinc.test.secretsanta.R
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.entity.Human
import com.squareup.picasso.Picasso

class HumanListAdapter(private val humans: List<Human>,
                       private val nickname: String,
                       private val humanListener: OnHumanItemClickListener)
    : RecyclerView.Adapter<HumanViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HumanViewHolder? {
        return HumanViewHolder(view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.meber_list_item_detail, parent, false),
                listener = object : HumanViewHolder.OnHumanItemClickListener {
                    override fun onClick(position: Int) {
                        humanListener.onClick(humans[position])
                    }
                })
    }

    override fun getItemCount(): Int = humans.size


    override fun onBindViewHolder(holder: HumanViewHolder, position: Int) {
        val human = humans[position]
        holder.isAdminView.visibility = if (human.admin) {
            View.VISIBLE
        } else {
            View.GONE
        }
        holder.memberNicknameView.text = human.nickname

        holder.isYourSecretView.visibility = if (human.giftedBy == nickname) {
            View.VISIBLE
        } else {
            View.GONE
        }

        Picasso.with(holder.view.context)
                .load(Constants.images[human.image.toInt()]!!)
                .into(holder.memberImage)
    }

    interface OnHumanItemClickListener {
        fun onClick(human: Human)
    }
}

class HumanViewHolder(val view: View, private val listener: OnHumanItemClickListener) : RecyclerView.ViewHolder(view) {
    val memberImage: ImageView = view.findViewById(R.id.member_image)
    val memberNicknameView: TextView = view.findViewById(R.id.nickname)
    val isYourSecretView: ImageView = view.findViewById(R.id.present)
    val isAdminView: ImageView = view.findViewById(R.id.admin)

    init {
        view.setOnClickListener {
            listener.onClick(adapterPosition)
        }
    }

    interface OnHumanItemClickListener {
        fun onClick(position: Int)
    }
}