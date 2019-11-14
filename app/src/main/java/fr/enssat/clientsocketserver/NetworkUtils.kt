package fr.enssat.clientsocketserver

import android.content.Context
import android.net.ConnectivityManager
import java.net.Inet4Address
import java.net.InetAddress


class NetworkUtils {
    companion object{
        fun getIpAddress(context: Context): InetAddress? {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val linkInfos = cm.getLinkProperties(cm.activeNetwork)?.linkAddresses
            val address =  linkInfos?.filter {it -> it.address is Inet4Address }?.single()
            return address?.address
        }
    }
}