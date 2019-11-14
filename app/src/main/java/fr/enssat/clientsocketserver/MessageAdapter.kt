package fr.enssat.clientsocketserver

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MessageViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val msgView = view.findViewById<TextView>(R.id.msg)

    fun setMessage(msg: String, listener: (String) -> Unit) {
        msgView.text = msg
        msgView.setOnClickListener { view ->
            Log.d("DEBUG", "onItemClick")
            //messageListener(msg)
        }
    }
}

class MessageAdapter(val listener: (String) -> Unit): RecyclerView.Adapter<MessageViewHolder>() {

    var list: List<String> = emptyList()
        set(l) {
            field = l
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.setMessage(list[position], listener)
    }
}