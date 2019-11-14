package fr.enssat.clientsocketserver

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClientViewModelFactory(val context: Context): ViewModelProvider.Factory {

    //factory crée afin de parvenir à passer un parameter en argument ici le context au view model...
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ClientViewModel(context.applicationContext) as T
    }
}