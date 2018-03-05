package com.softserveinc.test.secretsanta.service

import com.softserveinc.test.secretsanta.entity.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MessagesService {
    @POST("fcm/send")
    fun sendNotification(@Header("Content-Type") contentType: String,
                         @Header("Authorization") uid: String,
                         @Body message: Message): Call<Any?>
}