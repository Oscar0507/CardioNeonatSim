package com.example.simuladorlandscape.ui.parameters.innerParameters.MecRespParam

import androidx.lifecycle.ViewModel
import com.example.simuladorlandscape.LiveData_broadcastReceiverWifi

class MecRespViewModel:ViewModel() {
    val wifiState= LiveData_broadcastReceiverWifi()
}