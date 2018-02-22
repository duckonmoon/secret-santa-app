package com.softserveinc.test.secretsanta.viewmodel

import android.arch.lifecycle.ViewModel
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.entity.GroupFull


class HumanViewModel : ViewModel() {
    var groupFull: GroupFull? = null
    var group: Group? = null
}