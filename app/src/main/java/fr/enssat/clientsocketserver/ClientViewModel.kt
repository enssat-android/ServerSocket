package fr.enssat.clientsocketserver

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientViewModel(context: Context): ViewModel() {

    private val _client = ClientSocket(context, this::onReceiveMessage, this::onConnectionStatus)

    private val _msgSet = mutableSetOf<String>()
    private val _allMessages = MutableLiveData<List<String>>()

    private val _connected = MutableLiveData<Boolean>(false)
    val connected : LiveData<Boolean> get() = _connected

    val messages: LiveData<List<String>> get() = _allMessages

    override fun onCleared() {
        _client.stop("cleared")
        _connected.postValue(false)
        super.onCleared()
    }

    fun onReceiveMessage(msg: String) {
        _msgSet.add(msg)
        _allMessages.postValue(_msgSet.toList())
    }

    fun onConnectionStatus(bool:Boolean) {
        _connected.postValue(bool)
    }

    fun connect(serverIp:String, serverPort:Int?){
        _client.connect(serverIp, serverPort)
    }

    fun send(msg:String){
        _client.sendAndReceive(msg)
    }
}