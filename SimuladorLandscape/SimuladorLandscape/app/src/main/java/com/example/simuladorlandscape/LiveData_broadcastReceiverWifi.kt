package com.example.simuladorlandscape

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.lifecycle.LiveData
import kotlinx.coroutines.withTimeout
import java.math.BigInteger
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime


class LiveData_broadcastReceiverWifi():LiveData<Int>() {


    val c:Context= contextMainActivity
    val wifiManager:WifiManager=c.getSystemService(Context.WIFI_SERVICE) as WifiManager
    var wifiInfo=wifiManager.connectionInfo
    var StatusConected:Int=0 //Status Connected 0:Wifi off 1:Wifi on - Sim_off 2:Wifi on - Sim on
    var intentFilter:IntentFilter=IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)



   val wifi_Receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var ipAddressInt:IntArray= IntArray(4)
            val wifi_isConnected: Boolean= wifiManager.isWifiEnabled
            if (wifi_isConnected) {
                wifiInfo=wifiManager.connectionInfo
                val ipaddressscap=wifiInfo.ipAddress
                val ipByteArray:ByteArray=BigInteger.valueOf(ipaddressscap.toLong()).toByteArray()

                if (ipByteArray.size==4){
                for (n in 0..3){
                    ipAddressInt[n]=ipByteArray[n].toInt()
                    if (ipAddressInt[n]<0){ ipAddressInt[n]=ipAddressInt[n]+256 }
                                }
                val C=ipAddressInt[1]
                val B=ipAddressInt[2]
                val A=ipAddressInt[3]
                if (A==192 && B==168 && C==2) {   //MAC Address of Simulator "DE:4F:22:19:9B:71"
                    StatusConected=2              //Ip Address in range 192.168.2...
                } else {
                    StatusConected=1
                }}
                else{StatusConected=1}
            }
            else{StatusConected=0}

            postValue(StatusConected)
        }
    }

 override fun onActive() {
        super.onActive()
     intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
     intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        c.registerReceiver(
            wifi_Receiver,
            intentFilter
        )
    }

    override fun onInactive() {
        super.onInactive()
        try {
            c.unregisterReceiver(wifi_Receiver)
        } catch (e: Exception) {
        }
    }

}