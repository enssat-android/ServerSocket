package fr.enssat.clientsocketserver

import com.google.gson.*
import java.lang.reflect.Type

sealed class Message constructor(val type: String) {
    companion object {
        private val gson = GsonBuilder().registerTypeAdapter(Message::class.java, MessageSerializer()).create()
        fun toJson(src: Message) = gson.toJson(src)
        fun fromJson(str: String) = gson.fromJson<Message>(str, Message::class.java)
    }
}

    class UnknownMessage(): Message("unknown")

    class PingMessage(val msg: String): Message("ping")

    class PongMessage(val msg: String, val address: String): Message("pong")

    class VotesMessage(val msg: String, val liste: List<Votant>): Message("votes")

    data class Votant(val name: String, val note: Int)

class MessageSerializer: JsonDeserializer<Message> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext    ): Message {
        val type = json.asJsonObject["type"].asString
        return when (type) {
            "ping" -> context.deserialize<PingMessage>(json, PingMessage::class.java)
            "pong" -> context.deserialize<PongMessage>(json, PongMessage::class.java)
            "votes" -> context.deserialize<VotesMessage>(json, VotesMessage::class.java)
            else -> UnknownMessage()
        }
    }

}