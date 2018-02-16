package com.softserveinc.test.secretsanta.viewmodel

import android.arch.lifecycle.ViewModel
import com.softserveinc.test.secretsanta.entity.Member

class MembersViewModel : ViewModel() {
    val members = ArrayList<Member>()
    var currentImage = 3
}