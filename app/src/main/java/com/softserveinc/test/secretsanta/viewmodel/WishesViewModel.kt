package com.softserveinc.test.secretsanta.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableList

class WishesViewModel : ViewModel() {
    val wishes = ObservableArrayList<String>()

    var isChanged: Boolean = false
        private set

    init {
        wishes.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<String>>() {
            override fun onItemRangeRemoved(p0: ObservableArrayList<String>?, p1: Int, p2: Int) {
                isChanged = true
            }

            override fun onItemRangeMoved(p0: ObservableArrayList<String>?, p1: Int, p2: Int, p3: Int) {
                isChanged = true
            }

            override fun onItemRangeInserted(p0: ObservableArrayList<String>?, p1: Int, p2: Int) {
                isChanged = true
            }

            override fun onItemRangeChanged(p0: ObservableArrayList<String>?, p1: Int, p2: Int) {
                isChanged = true
            }

            override fun onChanged(p0: ObservableArrayList<String>?) {
                isChanged = true
            }
        })
    }

    fun reset(){
        isChanged = false
    }
}
