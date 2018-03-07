package com.softserveinc.test.secretsanta.util

import com.google.firebase.database.DataSnapshot
import com.softserveinc.test.secretsanta.entity.Group
import java.util.*


class Mapper private constructor() {

    companion object {
        fun mapFromDataSnapshotGroupsToStringGroups(dataSnapshot: DataSnapshot): ArrayList<Group> {
            val groups = ArrayList<Group>()
            dataSnapshot.children.mapTo(groups) { it.getValue(Group::class.java)!! }
            Collections.reverse(groups)
            return groups
        }
    }
}