package com.softserveinc.test.secretsanta.module


import com.google.firebase.auth.FirebaseAuth
import com.softserveinc.test.secretsanta.controller.MainController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule() {
    @Provides
    @Singleton
    fun provideApp() : MainController = MainController.INSTANCE

    val auth = FirebaseAuth.getInstance()!!
        @Provides @Singleton get
}