package com.softserveinc.test.secretsanta.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message() {
    @SerializedName("to")
    @Expose
    var to: String = ""

    @SerializedName("data")
    @Expose
    var data: Data = Data()

    constructor(to: String, body: String, title: String, admin: String, emptyMembers: List<String>) : this() {
        data.body = body
        data.title = title
        data.admin = admin
        data.emptyMembers.addAll(emptyMembers)
        this.to = to
    }
}

class Data {
    @SerializedName("body")
    @Expose
    var body: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("admin")
    @Expose
    var admin: String = ""

    @SerializedName("empty_members")
    @Expose
    var emptyMembers: ArrayList<String> = ArrayList()

}

