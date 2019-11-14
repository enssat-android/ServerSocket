package fr.enssat.clientsocketserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import fr.enssat.clientsocketserver.databinding.ActivityClientBinding

class ClientActivity : AppCompatActivity() {
    lateinit var binding: ActivityClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_client)

        val model = ViewModelProviders.of(this, ClientViewModelFactory(this)).get(ClientViewModel::class.java)

        binding.connectButton.setOnClickListener { view -> model.connect(binding.serverIpEditText.text.toString(),ServerSocket.PORT)}
        binding.sendButton.setOnClickListener { view -> model.send(binding.messageEditText.text.toString()) }

        val adapter = MessageAdapter {}
        binding.messageList.adapter = adapter

        model.messages.observe(this, Observer { list ->
            adapter.list = list
        })

        model.connected.observe(this, Observer { bool->
            binding.sendButton.setEnabled(bool)
        })
    }
 }
