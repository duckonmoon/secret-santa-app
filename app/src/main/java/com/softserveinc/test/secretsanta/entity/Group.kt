package com.softserveinc.test.secretsanta.entity

import java.io.Serializable


class Group : Serializable {
    var id = ""
    var activated = false
    var title = ""
    var members = 0
    var date_created = ""
}