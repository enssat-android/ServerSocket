package fr.enssat.clientsocketserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import fr.enssat.clientsocketserver.databinding.ActivityMainBinding
import fr.enssat.pokerplanning.vote.ServerViewModel
import fr.enssat.pokerplanning.vote.ServerViewModelFactory

class ServerActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = ViewModelProviders.of(this, ServerViewModelFactory(this)).get(ServerViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.serverTitle.text = "server listening on " + NetworkUtils.getIpAddress(this) + ": ${ServerSocket.PORT}"

        val adapter = MessageAdapter {}
        binding.messageList.adapter = adapter

        model.messages.observe(this, Observer { list ->
            adapter.list = list
        })
    }
 }
