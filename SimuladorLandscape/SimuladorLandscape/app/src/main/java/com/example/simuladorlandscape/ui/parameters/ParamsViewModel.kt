package com.example.simuladorlandscape.ui.parameters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simuladorlandscape.LiveData_broadcastReceiverWifi

class ParamsViewModel : ViewModel() {

    val wifiState= LiveData_broadcastReceiverWifi()

    /*var FreqCard:LiveData<String> = MutableLiveData("130.0")
    fun setFreqCard(valor:String){ FreqCard=MutableLiveData(valor) }

    var FreqResp:LiveData<String> = MutableLiveData("80.0")
    fun setFreqResp(valor:String){ FreqResp=MutableLiveData(valor) }

    var PresSyst:LiveData<String> = MutableLiveData("70.0")
    fun setPresSyst(valor:String){ PresSyst=MutableLiveData(valor) }

    var PresDiast:LiveData<String> = MutableLiveData("70.0")
    fun setPresDiast(valor:String){ PresDiast=MutableLiveData(valor) }

    var SatO2:LiveData<String> = MutableLiveData("70.0")
    fun setSatO2(valor:String){ SatO2=MutableLiveData(valor) }

    var etCO2:LiveData<String> = MutableLiveData("70.0")
    fun setetCO2(valor:String){ etCO2=MutableLiveData(valor) }

    var inCO2:LiveData<String> = MutableLiveData("70.0")
    fun setinCO2(valor:String){ inCO2=MutableLiveData(valor) }*/

    }