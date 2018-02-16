package com.softserveinc.test.secretsanta.entity

import java.io.Serializable


class Group : Serializable {
    companion object {
        const val ACTIVATED = 1
        const val PASSIVE = 0
        const val DELETED = -1
    }

    var id = ""
    var activated = PASSIVE
    var title = ""
    var members = 0
    var date_created = ""
    var imageCode = 3
}