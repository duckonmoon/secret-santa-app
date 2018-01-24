package com.softserveinc.test.secretsanta.service

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.softserveinc.test.secretsanta.constans.Constans


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
}