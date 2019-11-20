package fr.enssat.clientsocketserver

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ServerSocket(val context: Context, val listener: (String) -> Unit) {
    private val TAG = this.javaClass.simpleName

    companion object {
        val PORT = 6791
    }

    private lateinit var serverSocket: ServerSocket
    private val IPADDRESS = NetworkUtils.getIpAddress(context)
    private val MAX_CLIENT_BACK_LOG = 50
    private var loop = true

    //liste des clients connectés au serveur
    private val allClientsConnected = mutableListOf<Reply>()

    fun startListening() {
        serverSocket = ServerSocket(PORT, MAX_CLIENT_BACK_LOG, IPADDRESS)

        // écoute toutes les nouvelles demandes de connections clientes
        // et crée une socket locale au serveur, reply dédiée a ce nouveau client.
        Executors.newSingleThreadExecutor().execute {
            while (loop) {
                val newSocket = serverSocket.accept()
                allClientsConnected.add(Reply(newSocket, listener))
            }
        }
    }

    fun stopListening() {
        loop = false
        allClientsConnected.forEach { it.stop() }
        serverSocket.close()
    }


    class Reply(val socket: Socket, val listener: (String) -> Unit) {
        val TAG = this.javaClass.simpleName
        var loop = true
        val address = socket.inetAddress.address.toString()

        private val mainThread = object : Executor {
            val handler = Handler(Looper.getMainLooper())
            override fun execute(command: Runnable) {
                handler.post(command)
            }
        }

        init {
            mainThread.execute { listener("new client connection.") }
            Executors.newSingleThreadExecutor().execute {
                try {
                    while (loop) {
                        val buffer = ByteArray(2048)
                        val len = socket.getInputStream().read(buffer)
                        val msg = String(buffer, 0, len, StandardCharsets.UTF_8)

                        //affiche le message reçu dans l'ui
                        mainThread.execute { listener(msg) }

                        //répond avec message + adresse
                        val data = Message.toJson(PongMessage(msg, socket.remoteSocketAddress.toString()))
                            .toByteArray(StandardCharsets.UTF_8)
                        socket.getOutputStream().write(data)
                        socket.getOutputStream().flush()
                    }
                } catch (e: IOException) {
                    Log.d(TAG, "Client $address down")
                }
            }
        }

        fun stop() {
            loop = false
            socket.close()
        }
    }
}
