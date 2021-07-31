package com.example.simuladorlandscape.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.simuladorlandscape.LiveData_broadcastReceiverWifi
import com.example.simuladorlandscape.R
import com.example.simuladorlandscape.avisoNoconex
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Observer Wifi State


        val wifiObserver=Observer<Int> {Wifistate->
                                            when(Wifistate){
                                                0->{ wifi_Off()
                                                    sim_Off() }
                                                1->{ wifi_On()
                                                     sim_Off() }
                                                2->{ wifi_On()
                                                    sim_On() }
                                            }}

        homeViewModel.wifiState.observe(viewLifecycleOwner,wifiObserver)
    }

    fun wifi_On(){
        textViewStWiFi.setText("Wifi Habilitado")   //Wifi on- Sim off
        textViewIndWifi.setText("WiFi OK")
        avisoNoconex.visibility=View.INVISIBLE
        textViewStWiFi.setTextColor(Color.WHITE)
        textViewStWiFi.setBackgroundColor(Color.parseColor("#01579b"))
        imageViewWifiSt.setImageResource(R.drawable.ic_wifi_tethering_black_24dp)
    }
    fun wifi_Off(){
        textViewStWiFi.setText("Wifi Deshabilitado")
        textViewIndWifi.setText("Por favor habilite WiFi")
        avisoNoconex.visibility=View.VISIBLE
        textViewStWiFi.setBackgroundColor(Color.TRANSPARENT)
        textViewStWiFi.setTextColor(Color.BLACK)
        imageViewWifiSt.setImageResource(R.drawable.ic_portable_wifi_off_black_24dp)
    }
    fun sim_On() {
        textViewStSimCon.setText("Simulador ONLINE")   //Wifi on- Sim off
        textViewIndSim.setText("Simulador OK")
        textViewStSimCon.setTextColor(Color.WHITE)
        textViewStSimCon.setBackgroundColor(Color.parseColor("#01579b"))
        imageViewSimSt.setImageResource(R.drawable.ic_leak_add_black_24dp)
    }
    fun sim_Off() {
        textViewStSimCon.setText("Simulador OFFLINE")
        textViewIndSim.setText("Elija Red Wifi CardioNeonatSim Pass:12345678")
        textViewStSimCon.setBackgroundColor(Color.TRANSPARENT)
        textViewStSimCon.setTextColor(Color.BLACK)
        imageViewSimSt.setImageResource(R.drawable.ic_leak_remove_black_24dp)
    }

}