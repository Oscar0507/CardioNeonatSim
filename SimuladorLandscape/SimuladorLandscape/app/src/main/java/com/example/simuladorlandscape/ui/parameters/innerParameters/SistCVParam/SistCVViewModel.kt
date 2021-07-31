package com.example.simuladorlandscape.ui.parameters.innerParameters.SistCVParam

import androidx.lifecycle.ViewModel
import com.example.simuladorlandscape.LiveData_broadcastReceiverWifi

class SistCVViewModel:ViewModel() {

    val wifiState= LiveData_broadcastReceiverWifi()
}