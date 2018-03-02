package com.softserveinc.test.secretsanta.service

import com.softserveinc.test.secretsanta.entity.MessageHolder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MessagesService {
    @POST("./messages:send")
    fun sendNotification(@Header("Content-Type") contentType : String,
                         @Header("Authorization") uid: String,
                         @Body message : MessageHolder) : Call<Any?>
}