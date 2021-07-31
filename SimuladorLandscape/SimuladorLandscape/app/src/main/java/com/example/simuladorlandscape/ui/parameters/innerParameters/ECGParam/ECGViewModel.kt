package com.example.simuladorlandscape.ui.parameters.innerParameters.ECGParam

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simuladorlandscape.LiveData_broadcastReceiverWifi
import kotlinx.coroutines.Dispatchers


class ECGViewModel : ViewModel() {

    val wifiState= LiveData_broadcastReceiverWifi()

}