package com.softserveinc.test.secretsanta.service

import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
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
        const val GIFTED_BY = "giftedBy"
        const val RANDOMIZE = "randomize"
        const val PREFERENCES = "preferences"
        const val IMAGE = "image"
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
                        .setPhotoUri(Uri.parse(0.toString()))
                        .build())

        database.getReference(Constants.NICKNAMES)
                .child(nickname)
                .setValue(0)
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

    fun getUserNickname(): String? {
        return auth.currentUser!!.displayName
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

        informNewMembersForGroupInvitation(members, groupId, groupTitle, members.size)
    }

    private fun createGroup(members: ArrayList<Member>, groupTitle: String): String {
        val dbReference = database.getReference(Constants.GROUPS)

        val group = GroupFull()
        group.id = dbReference.push().key

        group.title = groupTitle
        for (member in members) {
            val human = Human()
            human.nickname = member.name
            human.image = member.imagePath
            group.humans[human.nickname] = human
        }
        val human = group.humans[auth.currentUser!!.displayName]!!
        human.admin = true

        dbReference.child(group.id).setValue(group)

        return group.id
    }

    private fun informNewMembersForGroupInvitation(members: ArrayList<Member>, groupId: String,
                                                   groupTitle: String, membersCount: Int) {
        val dbReference = database.getReference(Constants.NICKNAME)

        for (member in members) {
            val group = Group()
            group.id = groupId
            group.activated = Group.PASSIVE
            group.title = groupTitle
            group.date_created = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            group.members = membersCount

            dbReference
                    .child(member.name)
                    .child(Constants.GROUPS)
                    .child(groupId)
                    .setValue(group)
        }
        val group = Group()
        group.id = groupId
        group.activated = Group.ACTIVATED
        group.title = groupTitle
        group.date_created = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        group.members = membersCount

        dbReference
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .child(groupId)
                .setValue(group)
    }

    fun getCurrentUserAsMember(): Member {
        val member = Member()
        member.name = auth.currentUser!!.displayName!!
        member.imagePath = auth.currentUser!!.photoUrl.toString()
        return member
    }

    fun getAllActivatedGroups(listener: ValueEventListener) {
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .orderByChild(ACTIVATED)
                .equalTo(Group.ACTIVATED.toDouble())
                .addListenerForSingleValueEvent(listener)
    }

    fun getAllNotActivatedGroups(listener: ValueEventListener) {
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .orderByChild(ACTIVATED)
                .equalTo(Group.PASSIVE.toDouble())
                .addListenerForSingleValueEvent(listener)
    }

    fun getTrash(listener: ValueEventListener) {
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .orderByChild(ACTIVATED)
                .equalTo(Group.DELETED.toDouble())
                .addListenerForSingleValueEvent(listener)
    }

    fun updateGroupActivationStatus(group: Group) {
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .child(group.id)
                .child(ACTIVATED)
                .setValue(group.activated)
    }

    fun getGroupInfo(groupId: String, listener: ValueEventListener) {
        database.getReference(Constants.GROUPS)
                .child(groupId)
                .addListenerForSingleValueEvent(listener)
    }

    fun sendPasswordRestoreEmail(email: String, listener: OnCompleteListener<Void?>) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener)
    }

    fun randomizeGroup(group: GroupFull) {
        group.randomize = true
        val dbReference = database.getReference(Constants.GROUPS)
                .child(group.id)
        for (human in group.humans.values) {
            dbReference.child(Constants.HUMANS)
                    .child(human.nickname)
                    .child(GIFTED_BY)
                    .setValue(human.giftedBy)
        }
        dbReference.child(RANDOMIZE)
                .setValue(group.randomize)
    }

    fun setMyPreferences(groupFull: GroupFull, me: Human, wishes: ArrayList<String>) {
        me.preferences = wishes
        database.getReference(Constants.GROUPS)
                .child(groupFull.id)
                .child(Constants.HUMANS)
                .child(getUserNickname())
                .child(PREFERENCES)
                .setValue(wishes)
    }

    fun moveGroupToTrash(group: Group) {
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .child(group.id)
                .child(ACTIVATED)
                .setValue(Group.DELETED)
    }

    fun updateCurrentPhoto(photoInt: Int) {
        updateProfile(photoInt)

        updateNicknames(photoInt)

        getAllGroupsAndUpdate(photoInt)
    }

    private fun getAllGroupsAndUpdate(photoInt: Int) {
        database.getReference(Constants.NICKNAME)
                .child(auth.currentUser!!.displayName)
                .child(Constants.GROUPS)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        Log.e("FirebaseService", "Error update")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        if (dataSnapshot != null) {
                            updateEachGroup(dataSnapshot, photoInt)
                        }
                    }
                })
    }

    private fun updateEachGroup(dataSnapshot: DataSnapshot, photoInt: Int) {
        dataSnapshot.children
                .map { it.getValue(Group::class.java) }
                .forEach { updateGroupValue(it!!,photoInt) }
    }

    private fun updateGroupValue(group: Group, photoInt: Int) {
        database.getReference(Constants.GROUPS)
                .child(group.id)
                .child(Constants.HUMANS)
                .child(auth.currentUser!!.displayName)
                .child(IMAGE)
                .setValue(photoInt.toString())
    }

    private fun updateNicknames(photoInt: Int) {
        database.getReference(Constants.NICKNAMES)
                .child(auth.currentUser!!.displayName)
                .setValue(photoInt)
    }

    private fun updateProfile(photoInt: Int) {
        auth.currentUser!!.updateProfile(
                UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(photoInt.toString()))
                        .build())
    }
}