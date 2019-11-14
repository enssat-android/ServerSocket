package fr.enssat.clientsocketserver

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ClientSocket(val context: Context, val messageListener: (String) -> Unit, val connectionListener: (Boolean) -> Unit) {
    private val TAG = this.javaClass.simpleName

    private lateinit var socket: Socket

    private val mainThread = object : Executor {
        val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }

    fun stop(msg:String) {
        socket.close()
    }

    fun connect(serverIp:String, serverPort:Int?) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val address = InetAddress.getByName(serverIp)
                val port = serverPort ?: 6791
                Log.d(TAG, "try connecting to $address:$port")
                socket = Socket(address, port)
                //sendAndReceive("coucou")
                mainThread.execute { connectionListener(true) }
            }
            catch(ex:Exception) {
                val msg = ex.message ?: "unable to connect server"
                mainThread.execute { messageListener(msg) }
                mainThread.execute { connectionListener(false) }
            }
        }
    }

    fun sendAndReceive(msg: String) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val data = msg.toByteArray(StandardCharsets.UTF_8)
                socket.getOutputStream().write(data)
                socket.getOutputStream().flush()

                val buffer = ByteArray(2048)
                val len = socket.getInputStream().read(buffer)
                val str = String(buffer, 0, len, StandardCharsets.UTF_8)

                //affiche le message reçu dans l'ui
                mainThread.execute { messageListener(str) }

            } catch (e: IOException) {
                val msg = e.message ?: "unable to send to or receive from server"
                mainThread.execute { messageListener(msg) }
                mainThread.execute { connectionListener(false) }
                stop(msg)
            }
        }
    }
}
