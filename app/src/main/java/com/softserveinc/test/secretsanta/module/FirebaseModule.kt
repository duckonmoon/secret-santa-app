package com.softserveinc.test.secretsanta.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.softserveinc.test.secretsanta.service.FirebaseService
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseService(auth: FirebaseAuth,database: FirebaseDatabase) : FirebaseService{
        return FirebaseService(auth = auth,database = database)
    }
}