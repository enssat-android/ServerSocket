package fr.enssat.clientsocketserver

import android.content.Context
import android.net.ConnectivityManager
import java.net.Inet4Address
import java.net.InetAddress
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter


class NetworkUtils {
    companion object{
        fun getIpAddress(context: Context): InetAddress? {
            var result : InetAddress?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val linkInfos = cm.getLinkProperties(cm.activeNetwork)?.linkAddresses
                val address = linkInfos?.filter { it -> it.address is Inet4Address }?.single()
                result = address?.address
            }
            else {

                val wm = context.getSystemService(WIFI_SERVICE) as WifiManager
                val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
                result = InetAddress.getByName(ip)
            }

            return result
        }
    }
}