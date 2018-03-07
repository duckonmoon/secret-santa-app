package com.softserveinc.test.secretsanta.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.softserveinc.test.secretsanta.constans.Constants

class Message() {

    @SerializedName("to")
    @Expose
    var to: String = ""

    @SerializedName("data")
    @Expose
    var data: Data = Data()

    constructor(admin: String,
                emptyMembers: List<String>,
                group: Group) : this() {
        to = Constants.TOPICS + group.id
        data.admin = admin

        data.title = group.title

        data.emptyMembers.addAll(emptyMembers)

        data.groupId = group.id
        data.groupActivated = group.activated
        data.groupDateCreated = group.date_created
        data.groupMembers = group.members
        data.groupImageCode = group.imageCode
    }
}

class Data {

    companion object {
        const val BODY = "body"
        const val TITLE = "title"
        const val ADMIN = "admin"
        const val EMPTY_MEMBERS = "empty_members"
        const val GROUP_ID = "group_id"
        const val GROUP_ACTIVATED = "group_activated"
        const val GROUP_MEMBERS = "group_members"
        const val GROUP_DATE_CREATED = "group_date_created"
        const val GROUP_IMAGE_CODE = "group_image_code"
    }


    @SerializedName(BODY)
    @Expose
    var body: String = "Admin asks you to fill wishlist"
    @SerializedName(TITLE)
    @Expose
    var title: String = ""

    @SerializedName(ADMIN)
    @Expose
    var admin: String = ""

    @SerializedName(EMPTY_MEMBERS)
    @Expose
    var emptyMembers: ArrayList<String> = ArrayList()


    @SerializedName(GROUP_ID)
    @Expose
    var groupId = ""

    @SerializedName(GROUP_ACTIVATED)
    @Expose
    var groupActivated = Group.PASSIVE

    @SerializedName(GROUP_MEMBERS)
    @Expose
    var groupMembers = 0

    @SerializedName(GROUP_DATE_CREATED)
    @Expose
    var groupDateCreated = ""

    @SerializedName(GROUP_IMAGE_CODE)
    @Expose
    var groupImageCode = 3

}

