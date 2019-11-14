package fr.enssat.clientsocketserver

import com.google.gson.*

data class Message constructor(var msg:String, var address:String) {
    companion object {
        private val gson = Gson()
        fun toJson(src: Message) = gson.toJson(src)
        fun fromJson(str: String) = gson.fromJson<Message>(str, Message::class.java)
    }
}
