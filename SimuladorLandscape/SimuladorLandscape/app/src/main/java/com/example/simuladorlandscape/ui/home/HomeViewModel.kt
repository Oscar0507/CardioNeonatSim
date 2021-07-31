package com.example.simuladorlandscape.ui.home

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simuladorlandscape.LiveData_broadcastReceiverWifi
import kotlinx.coroutines.Dispatchers

var toOn:Boolean=false

class HomeViewModel : ViewModel() {

    val wifiState= LiveData_broadcastReceiverWifi()

}