package com.softserveinc.test.secretsanta.entity

import java.io.Serializable

class GroupFull : Serializable {
    var id = ""
    var title = ""
    var randomize = false
    var humans = HashMap<String, Human>()
    var imageCode = 3
}