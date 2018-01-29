package com.softserveinc.test.secretsanta.service

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.constans.Constants
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.entity.GroupFull
import com.softserveinc.test.secretsanta.entity.Human
import com.softserveinc.test.secretsanta.entity.Member
import java.text.SimpleDateFormat
import java.util.*


class FirebaseService(private val database: FirebaseDatabase, private val auth: FirebaseAuth) {

    companion object {
        const val ID = "id"
        const val ACTIVATED = "activated"
        const val TITLE = "title"
        const val MEMBERS = "members"
        const val DATE_CREATED = "date_created"
    }


    fun checkIfNickExists(listener: ValueEventListener, nickname: String) {
        database.getReference(Constants.NICKNAMES)
                .child(nickname)
                .addListenerForSingleValueEvent(listener)
    }

    fun createUserWithEmailAndPassword(email: String, password: String, listener: OnCompleteListener<AuthResult?>) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener)
    }

    fun sendEmailVerification() {
        auth.currentUser!!.sendEmailVerification()
    }

    fun setUserNickname(nickname: String) {
        auth.currentUser!!.updateProfile(
                UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname)
                        .build())

        database.getReference(Constants.NICKNAMES)
                .child(nickname)
                .setValue(auth.currentUser!!.uid)
    }

    fun checkIfCurrentUserExists(): Boolean {
        return auth.currentUser != null
    }

    fun checkIfEmailIsVerified(): Boolean {
        return auth.currentUser!!.isEmailVerified
    }

    fun getUserEmail(): String? {
        return auth.currentUser!!.email
    }

    fun signInWithEmailAndPassword(email: String, password: String, onCompleteListener: OnCompleteListener<AuthResult?>) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
    }

    fun getAllNicknames(listener: ValueEventListener, startAt: String) {
        database.getReference(Constants.NICKNAMES)
                .orderByKey()
                .startAt(startAt)
                .limitToFirst(10)
                .addListenerForSingleValueEvent(listener)
    }

    fun createNewGroup(members: ArrayList<Member>, groupTitle: String) {
        val groupId = createGroup(members, groupTitle)

        informNewMembersForGroupInvitation(members, groupId, groupTitle,members.size)
    }

    private fun createGroup(members: ArrayList<Member>, groupTitle: String): String {
        val dbReference = database.getReference(Constants.GROUPS)

        val group = GroupFull()
        group.id = dbReference.push().key

        group.title = groupTitle
        for (member in members) {
            val human = Human()
            human.nickname = member.name
            group.humans[human.nickname] = human
        }
        val human = group.humans[auth.currentUser!!.displayName]!!
        human.admin = true
        human.activated = true

        dbReference.child(group.id).setValue(group)

        return group.id
    }

    private fun informNewMembersForGroupInvitation(members: ArrayList<Member>, groupId: String,
                                                   groupTitle: String, membersCount : Int) {
        val dbReference = database.getReference(Constants.NICKNAME)

        for (member in members) {
            val group = HashMap<String, Any>()
            group[ID] = groupId
            group[ACTIVATED] = false
            group[TITLE] = groupTitle
            group[DATE_CREATED] = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            group[MEMBERS] = membersCount

            dbReference
                    .child(member.name)
                    .child(Constants.GROUPS)
                    .child(groupId)
                    .setValue(group)
        }
        val group = HashMap<String, Any>()
        group[ID] = groupId
        group[ACTIVATED] = true
        group[TITLE] = groupTitle
        group[DATE_CREATED] = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(Date())
        group[MEMBERS] = membersCount

        dbReference
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .child(groupId)
                .setValue(group)
    }

    fun getCurrentUserAsMember(): Member {
        val member = Member()
        member.name = auth.currentUser!!.displayName!!
        return member
    }

    fun getAllActivatedGroups(listener: ValueEventListener) {
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .orderByChild(ACTIVATED)
                .equalTo(true)
                .addListenerForSingleValueEvent(listener)
    }

    fun getAllNotActivatedGroups(listener: ValueEventListener){
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .orderByChild(ACTIVATED)
                .equalTo(false)
                .addListenerForSingleValueEvent(listener)
    }

    fun updateGroupActivationStatus(group : Group) {
        database.getReference(Constants.GROUPS)
                .child(group.id)
                .child(Constants.HUMANS)
                .child(auth.currentUser!!.displayName)
                .child(ACTIVATED)
                .setValue(group.activated)

        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .child(group.id)
                .child(ACTIVATED)
                .setValue(group.activated)
    }

    fun getGroupInfo(groupId: String,listener: ValueEventListener){
        database.getReference(Constants.GROUPS)
                .child(groupId)
                .addListenerForSingleValueEvent(listener)
    }

}