package com.example.simuladorlandscape.ui.plots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simuladorlandscape.LiveData_broadcastReceiverWifi

class PlotsViewModel : ViewModel() {

    val wifiState= LiveData_broadcastReceiverWifi()
}