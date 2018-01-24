package com.softserveinc.test.secretsanta.service

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.constans.Constans
import com.softserveinc.test.secretsanta.entity.Group
import com.softserveinc.test.secretsanta.entity.Human
import com.softserveinc.test.secretsanta.entity.Member


class FirebaseService(private val database: FirebaseDatabase,private val auth: FirebaseAuth) {
    fun checkIfNickExists(listener: ValueEventListener,nickname: String){
        database.getReference(Constans.NICKNAMES)
                .child(nickname)
                .addListenerForSingleValueEvent(listener)
    }

    fun createUserWithEmailAndPassword(email : String, password : String, listener : OnCompleteListener<AuthResult?>){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(listener)
    }

    fun sendEmailVerification(){
        auth.currentUser!!.sendEmailVerification()
    }

    fun setUserNickname(nickname : String){
        auth.currentUser!!.updateProfile(
                UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname)
                        .build())

        database.getReference(Constans.NICKNAMES)
                .child(nickname)
                .setValue(auth.currentUser!!.uid)
    }

    fun checkIfCurrentUserExists() : Boolean {
        return auth.currentUser != null
    }

    fun checkIfEmailIsVerified() : Boolean{
        return auth.currentUser!!.isEmailVerified
    }

    fun getUserEmail() : String? {
        return auth.currentUser!!.email
    }

    fun signInWithEmailAndPassword(email: String,password: String,onCompleteListener: OnCompleteListener<AuthResult?>){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
    }

    fun getAllNicknames(listener: ValueEventListener,startAt : String){
        database.getReference(Constans.NICKNAMES)
                .orderByKey()
                .startAt(startAt)
                .limitToFirst(10)
                .addListenerForSingleValueEvent(listener)
    }

    fun createNewGroup(members: ArrayList<Member>, groupTitle: String) {
        val groupId = createGroup(members,groupTitle)

        informNewMembersForGroupInvitation(members,groupId)
    }

    private fun createGroup(members: ArrayList<Member>, groupTitle: String) : String {
        val dbReference = database.getReference(Constans.GROUPS)

        val group = Group()
        group.id = dbReference.push().key

        group.title = groupTitle
        for (member in members){
            val human = Human()
            human.nickname = member.name
            group.humans.add(human)
        }
        val human = group.humans.find{(it.nickname == auth.currentUser!!.displayName)}!!
        human.admin = true
        human.activated = true

        dbReference.child(group.id).setValue(group)

        return group.id
    }

    private fun informNewMembersForGroupInvitation(members: ArrayList<Member>, groupId: String) {
        val dbReference = database.getReference(Constans.NICKNAME)

        for (member in members){
            dbReference
                    .child(member.name)
                    .child(Constans.GROUPS)
                    .child(groupId)
                    .setValue(false)
        }

        dbReference
                .child(auth.currentUser!!.displayName)
                .child(Constans.GROUPS)
                .child(groupId)
                .setValue(true)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){

                    } else {

                    }
                }
    }

    fun getCurrentUserAsMember(): Member {
        val member = Member()
        member.name = auth.currentUser!!.displayName!!
        return member
    }
}