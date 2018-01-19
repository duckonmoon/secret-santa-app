package com.softserveinc.test.secretsanta.module


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.softserveinc.test.secretsanta.controller.MainController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()!!

    @Provides
    @Singleton
    fun provideApp() : MainController = MainController.INSTANCE

    val auth = FirebaseAuth.getInstance()!!
        @Provides @Singleton get

    val databaseUsers = database.getReference("users")!!
        @Provides @Singleton get

    val databaseGroups = database.getReference("groups")!!
        @Provides @Singleton get
}