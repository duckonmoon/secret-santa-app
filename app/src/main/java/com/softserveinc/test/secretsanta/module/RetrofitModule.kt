package com.softserveinc.test.secretsanta.module

import com.softserveinc.test.secretsanta.service.MessagesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {
    val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()!!
        @Provides @Singleton get


    @Provides
    fun messageService(retrofit: Retrofit): MessagesService {
        return retrofit.create(MessagesService::class.java)
    }
}