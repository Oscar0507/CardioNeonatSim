package com.example.simuladorlandscape

import android.os.StrictMode
import androidx.lifecycle.LiveData
import com.example.simuladorlandscape.ui.plots.PlotsFragment
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.SocketException


//class UDPComm():LiveData<Boolean>(){
class UDPComm() {

    //var data_ready:Boolean=UDPReceiver().dataReaded

    fun sendUDP(msg: String){
        // Hack Prevent crash (sending should be done using an async task)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            socket.broadcast = true
            val sendData = msg.toByteArray()
            val sendPacket = DatagramPacket(
                sendData,
                sendData.size,
                InetAddress.getByName(simipaddr),
                simPort)
            socket.send(sendPacket)
            println("fun sendBroadcast: packet sent to: " + InetAddress.getByName(simipaddr) + ":" + simPort)
        } catch (e: IOException) {
            println("open fun SendUDP catch exception." + e.toString())
            e.printStackTrace()
        }}



      class UDPReceiver() : Runnable, MainActivity() {
        val buffer = ByteArray(64)
        var dataReaded:Boolean=false

        override fun run() {
            println("${Thread.currentThread()} Runnable Thread Started.")
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            socket.broadcast = true
            while (recv_on) {
                receiveUDP()}
        }


        fun receiveUDP(){
            try {
                val packet = DatagramPacket(buffer, buffer.size)
                try {
                    socket.setSoTimeout(1100)
                } catch (e1: SocketException) {
                    e1.printStackTrace()
                } //Capturador de excepciones de timeout

                socket.receive(packet)      //Lectura UDP
                lectura = true                //Reception Flag
                //dataReaded= lectura
                dat_buff = packet.data
                var signal:Char= dat_buff[0].toChar()
                when (signal){
                    'A'->{
                        dat_buff.copyInto(ecg_buff,0,0,64)
                        }
                    'B'->{
                        dat_buff.copyInto(pulse_buff,0,0,64)}
                    'C'->{
                        dat_buff.copyInto(volResp_buff,0,0,64)}
                    'D'->{
                        dat_buff.copyInto(press_buff,0,0,64)}
                    'E'->{
                        dat_buff.copyInto(satO_buff,0,0,64)}
                    'F'->{
                        dat_buff.copyInto(CO2_buff,0,0,64)}
                    'G'->{
                        dat_buff.copyInto(intRR_buff,0,0,64)}
                    'H'->{
                        dat_buff.copyInto(allVar_buff,0,0,64)
                        allVar_list=String(allVar_buff).split("@")
                        graphFlag=true}

                         }

                dat_buff = zero_buff
                if (graphFlag) { graphFlag=false
                                 runOnUiThread { PlotsFragment().graficar() } }

            } catch (e: SocketException) {
                println("open fun receiveUDP catch exception." + e.toString())
                e.printStackTrace() }
              catch (e: IOException) {
                println("open fun receiveUDP catch exception." + e.toString())
                e.printStackTrace() }
        }

    }
}