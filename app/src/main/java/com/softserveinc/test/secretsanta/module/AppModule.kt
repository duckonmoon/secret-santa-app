package com.softserveinc.test.secretsanta.module


import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.softserveinc.test.secretsanta.application.App
import com.softserveinc.test.secretsanta.constans.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()!!

    @Provides
    @Singleton
    fun provideApp(): App = App.INSTANCE

    val auth = FirebaseAuth.getInstance()!!
        @Provides @Singleton get

    val databaseUsers = database
        @Provides @Singleton get

    @Provides
    @Singleton
    fun provideSharedPreferences(app: App): SharedPreferences {
        return app.getSharedPreferences(Constants.MAIN_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}