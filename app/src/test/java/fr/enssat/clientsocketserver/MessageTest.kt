package fr.enssat.clientsocketserver

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Assert.*
import org.junit.Test

class MessageTest {

    val messageType = object : TypeToken<Message>() {}.type

    fun getGson(): Gson {
        return GsonBuilder().registerTypeAdapter(messageType, MessageSerializer()).create()
    }

    @Test
    fun testPingMessageSerialisation() {
        val ping = PingMessage("coucou")
        val json = Message.toJson(ping)
        println("json du ping: $json")
    }

    @Test
    fun testPingMessageDeserialisation() {
        val string = """{"msg":"coucou","type":"ping"}"""
        val message = Message.fromJson(string)
        println("message du ping: $message")
    }

    @Test
    fun testPongMessageSerialisation() {
        val pong = PongMessage("retour du ping", "192.164.1.100")
        val json = Message.toJson(pong)
        println("json du pong: $json")
    }

    @Test
    fun testPongMessageDeserialisation() {
        val string = """{"msg":"retour du ping","address":"192.164.1.100","type":"pong"}"""
        val message = Message.fromJson(string)
        println("message du pong: $message")
    }

    @Test
    fun testVotesMessageSerialisation() {
        val liste = listOf(
            Votant("gilles", 2),
            Votant("guillaume", 5),
            Votant("pierre", 3)
        )
        val votes = VotesMessage("feature 24", liste)
        val json = Message.toJson(votes)
        println("json du votes: $json")
    }

    @Test
    fun testVotesMessageDeserialisation() {
        val string = """{"msg":"feature 24","liste":[{"name":"gilles","note":2},{"name":"guillaume","note":5},{"name":"pierre","note":3}],"type":"votes"}"""
        val message = Message.fromJson(string)
        println("message du votes: $message")
        val msg = message as VotesMessage
        assertEquals(3, msg.liste.size)
        assertEquals("guillaume", msg.liste[1].name)
        assertEquals(5, msg.liste[1].note)
    }

}