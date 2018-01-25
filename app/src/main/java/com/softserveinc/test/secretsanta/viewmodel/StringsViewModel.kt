package com.softserveinc.test.secretsanta.viewmodel

import android.arch.lifecycle.ViewModel
import com.softserveinc.test.secretsanta.entity.Group

class StringsViewModel : ViewModel() {
    var groups = ArrayList<Group>()
}
