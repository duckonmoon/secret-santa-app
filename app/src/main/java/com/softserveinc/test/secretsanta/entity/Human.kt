package com.softserveinc.test.secretsanta.entity

import java.io.Serializable

//TODO ADD activated field with receiver id
class Human : Serializable {
    var nickname = ""
    var image = ""
    var admin = false
    var preferences = ArrayList<String>()
    var giftedBy = ""
}