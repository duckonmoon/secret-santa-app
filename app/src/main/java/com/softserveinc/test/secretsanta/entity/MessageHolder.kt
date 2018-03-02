package com.softserveinc.test.secretsanta.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MessageHolder {
    @SerializedName("message")
    @Expose
    var message: Message = Message()

    class MessageBuilder() {
        private val messageHolder = MessageHolder()
        private val message = Message()
        private val notification = Notification()

        fun setTopic(topic: String): MessageBuilder {
            message.to = topic
            return this
        }

        fun setBody(body: String): MessageBuilder {
            notification.body = body
            return this
        }

        fun setTitle(title: String): MessageBuilder {
            notification.title = title
            return this
        }

        fun build(): MessageHolder {
            return messageHolder
        }

        init {
            message.notification = notification
            messageHolder.message = message
        }
    }
}

class Message {
    @SerializedName("topic")
    @Expose
    var to: String = ""
    @SerializedName("notification")
    @Expose
    var notification: Notification = Notification()
}

class Notification {
    @SerializedName("body")
    @Expose
    var body: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""
}

