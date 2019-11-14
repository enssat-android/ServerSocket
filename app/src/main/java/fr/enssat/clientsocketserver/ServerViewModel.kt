package fr.enssat.pokerplanning.vote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.enssat.clientsocketserver.ServerSocket


class ServerViewModel(context: Context): ViewModel() {

    private val _msgSet = mutableSetOf<String>()
    private val _server = ServerSocket(context, this::onReceiveMessage)
    private val _allMessages = MutableLiveData<List<String>>()

    val messages: LiveData<List<String>> get() = _allMessages

    init {   _server.startListening()}

    override fun onCleared() {
        _server.stopListening()
        super.onCleared()
    }

    fun onReceiveMessage(msg: String) {
        _msgSet.add(msg)
        _allMessages.postValue(_msgSet.toList())
    }

}