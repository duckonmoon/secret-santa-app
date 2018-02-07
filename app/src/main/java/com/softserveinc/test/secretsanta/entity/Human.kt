package com.softserveinc.test.secretsanta.entity

import java.io.Serializable

class Human : Serializable{
    var nickname = ""
    var image = ""
    var activated = false
    var admin = false
    var preferences = ArrayList<String>()
    var giftedBy = ""
}