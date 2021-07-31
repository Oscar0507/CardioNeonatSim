package com.example.simuladorlandscape

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.simuladorlandscape.ui.parameters.innerParameters.ECGParam.ECGFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.net.DatagramSocket

lateinit var contextMainActivity: Context
//lateinit var navController:NavController

val pi:Float=Math.PI.toFloat()

val simPort:Int=8888
val simipaddr:String="192.168.2.22"
var socket = DatagramSocket(simPort)
lateinit var UDPReceiverThread:Thread
var recv_on:Boolean=false
var lectura:Boolean=false
var graphFlag:Boolean=false

var graficando:Boolean=false

var dat_buff: ByteArray = ByteArray(64)
var ecg_buff: ByteArray = ByteArray(64)
var pulse_buff: ByteArray = ByteArray(64)
var volResp_buff: ByteArray = ByteArray(64)
var press_buff: ByteArray = ByteArray(64)
var satO_buff: ByteArray = ByteArray(64)
var CO2_buff: ByteArray = ByteArray(64)
var intRR_buff: ByteArray = ByteArray(64)
var allVar_buff: ByteArray = ByteArray(64)
//var paramGrl_buff: ByteArray = ByteArray(64)
var allVar_list:List<String> = emptyList()
val zero_buff:ByteArray=ByteArray(64)

var time:Float=0F
var indexgraf:Int=0
var Tmax:Int=2

/* Parámetros */
var FreqCard:Float=130.0F
var FreqCard_Scen= floatArrayOf(130F,130F,130F,130F,130F,130F,130F,130F,130F,130F,130F)
var FreqResp:Float=80.0F
var FreqResp_Scen= floatArrayOf(80F,80F,80F,80F,80F,80F,80F,80F,80F,80F,80F)
var Peso:Float=3.4F
var Peso_Scen= floatArrayOf(3.4F,3.4F,3.4F,3.4F,3.4F,3.4F,3.4F,3.4F,3.4F,3.4F,3.4F)
var Temp:Float=37.4F
var Temp_Scen= floatArrayOf(37.4F,37.4F,37.4F,37.4F,37.4F,37.4F,37.4F,37.4F,37.4F,37.4F,37.4F)
var PH:Float=7.4F
var PH_Scen= floatArrayOf(7.4F,7.4F,7.4F,7.4F,7.4F,7.4F,7.4F,7.4F,7.4F,7.4F,7.4F)

var DesvFc:Float=5F     //1-20
var DesvFc_Scen= floatArrayOf(5F,5F,5F,5F,5F,5F,5F,5F,5F,5F,5F)
var F_lf:Float=0.1F     //0.05-0.15
var F_lf_Scen= floatArrayOf(0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F)
var F_hf:Float=0.5F     //0.16-2
var F_hf_Scen= floatArrayOf(0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F)
var Desv_lf:Float=0.1F  //0.01-0.5
var Desv_lf_Scen= floatArrayOf(0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F)
var Desv_hf:Float=0.1F  //0.01-0.5
var Desv_hf_Scen= floatArrayOf(0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F)
var LF_HF:Float=0.5F    //0.1-50
var LF_HF_Scen= floatArrayOf(0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F)

//Parámetros ECG
var ThetP:Float=0F      //0-2pi
var ThetP_Scen= floatArrayOf(0F,0F,0F,0F,0F,0F,0F,0F,0F,0F,0F)
var ThetQ:Float=Math.round(pi/4*100).toFloat()/100
var ThetQ_Scen= floatArrayOf(0.79F,0.79F,0.79F,0.79F,0.79F,0.79F,0.79F,0.79F,0.79F,0.79F,0.79F)
var ThetR:Float=Math.round(pi/3*100).toFloat()/100
var ThetR_Scen= floatArrayOf(1.05F,1.05F,1.05F,1.05F,1.05F,1.05F,1.05F,1.05F,1.05F,1.05F,1.05F)
var ThetS:Float=Math.round(5*pi/12*100).toFloat()/100
var ThetS_Scen= floatArrayOf(1.31F,1.31F,1.31F,1.31F,1.31F,1.31F,1.31F,1.31F,1.31F,1.31F,1.31F)
var ThetT:Float=Math.round(5*pi/6*100).toFloat()/100
var ThetT_Scen= floatArrayOf(2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F)

var a_P:Float=1.2F  //-10-30
var aP_Scen= floatArrayOf(1.2F,1.2F,1.2F,1.2F,1.2F,1.2F,1.2F,1.2F,1.2F,1.2F,1.2F)
var a_Q:Float=-5F
var aQ_Scen= floatArrayOf(-5F,-5F,-5F,-5F,-5F,-5F,-5F,-5F,-5F,-5F,-5F)
var a_R:Float=30F
var aR_Scen= floatArrayOf(30F,30F,30F,30F,30F,30F,30F,30F,30F,30F,30F)
var a_S:Float=-7.5F
var aS_Scen= floatArrayOf(-7.5F,-7.5F,-7.5F,-7.5F,-7.5F,-7.5F,-7.5F,-7.5F,-7.5F,-7.5F,-7.5F)
var a_T:Float=0.75F
var aT_Scen= floatArrayOf(0.75F,0.75F,0.75F,0.75F,0.75F,0.75F,0.75F,0.75F,0.75F,0.75F,0.75F)

var b_P:Float=0.25F  //0-2
var bP_Scen= floatArrayOf(0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F)
var b_Q:Float=0.1F
var bQ_Scen= floatArrayOf(0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F)
var b_R:Float=0.1F
var bR_Scen= floatArrayOf(0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F)
var b_S:Float=0.1F
var bS_Scen= floatArrayOf(0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F,0.1F)
var b_T:Float=0.4F
var bT_Scen= floatArrayOf(0.4F,0.4F,0.4F,0.4F,0.4F,0.4F,0.4F,0.4F,0.4F,0.4F,0.4F)
//Parámetros Pulso
var ThetOI:Float=Math.round(pi/2*100).toFloat()/100      //0-2pi
var ThetOI_Scen= floatArrayOf(1.57F,1.57F,1.57F,1.57F,1.57F,1.57F,1.57F,1.57F,1.57F,1.57F,1.57F)
var ThetOR:Float=Math.round(5*pi/4*100).toFloat()/100
var ThetOR_Scen= floatArrayOf(3.93F,3.93F,3.93F,3.93F,3.93F,3.93F,3.93F,3.93F,3.93F,3.93F,3.93F)

var a_OI:Float=2F  //-10-30
var aOI_Scen= floatArrayOf(2F,2F,2F,2F,2F,2F,2F,2F,2F,2F,2F)
var a_OR:Float=1F
var aOR_Scen= floatArrayOf(1F,1F,1F,1F,1F,1F,1F,1F,1F,1F,1F)

var b_OI:Float=1F  //0-2
var bOI_Scen= floatArrayOf(1F,1F,1F,1F,1F,1F,1F,1F,1F,1F,1F)
var b_OR:Float=1F
var bOR_Scen= floatArrayOf(1F,1F,1F,1F,1F,1F,1F,1F,1F,1F,1F)

//Parámetros Mec. Resp
var DeltaPmax:Float=1F
var DPmax_Scen= floatArrayOf(1F,1F,1F,1F,1F,1F,1F,1F,1F,1F,1F)
var Patm:Float=760F
var Patm_Scen= floatArrayOf(760F,760F,760F,760F,760F,760F,760F,760F,760F,760F,760F)
var Tinsp:Float=0.3F
var Tinsp_Scen= floatArrayOf(0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F)
var Rresp:Float=0.014F  //0.014-0.029
var Rresp_Scen= floatArrayOf(0.014F,0.014F,0.014F,0.014F,0.014F,0.014F,0.014F,0.014F,0.014F,0.014F,0.014F)
var CapPulm:Float=65F   //65-70ml/Kg
var CapP_Scen= floatArrayOf(65F,65F,65F,65F,65F,65F,65F,65F,65F,65F,65F)
var VD:Float=0.3F
var VD_Scen= floatArrayOf(0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F,0.3F)

//Parámetros Cardiovasculares

var cHbSang:Float=0.18F
var cHb_Scen= floatArrayOf(0.18F,0.18F,0.18F,0.18F,0.18F,0.18F,0.18F,0.18F,0.18F,0.18F,0.18F)
var ConsTotO2:Float=0.14F
var cTO2_Scen= floatArrayOf(0.14F,0.14F,0.14F,0.14F,0.14F,0.14F,0.14F,0.14F,0.14F,0.14F,0.14F)
var Shunt:Float=0.01F
var Sh_Scen= floatArrayOf(0.01F,0.01F,0.01F,0.01F,0.01F,0.01F,0.01F,0.01F,0.01F,0.01F,0.01F)
var IndResp:Float=0.8F
var indR_Scen= floatArrayOf(0.8F,0.8F,0.8F,0.8F,0.8F,0.8F,0.8F,0.8F,0.8F,0.8F,0.8F)

var Eaimax:Float=3.72F
var Eaimax_Scen= floatArrayOf(3.72F,3.72F,3.72F,3.72F,3.72F,3.72F,3.72F,3.72F,3.72F,3.72F,3.72F)
var Eaimin:Float=3.5F
var Eaimin_Scen= floatArrayOf(3.5F,3.5F,3.5F,3.5F,3.5F,3.5F,3.5F,3.5F,3.5F,3.5F,3.5F)
var Evimax:Float=53.1F
var Evimax_Scen= floatArrayOf(53.1F,53.1F,53.1F,53.1F,53.1F,53.1F,53.1F,53.1F,53.1F,53.1F,53.1F)
var Evimin:Float=2.63F
var Evimin_Scen= floatArrayOf(2.63F,2.63F,2.63F,2.63F,2.63F,2.63F,2.63F,2.63F,2.63F,2.63F,2.63F)
var Eait:Float=13.64F
var Eait_Scen= floatArrayOf(13.64F,13.64F,13.64F,13.64F,13.64F,13.64F,13.64F,13.64F,13.64F,13.64F,13.64F)
var Eaet:Float=5.8F
var Eaet_Scen= floatArrayOf(5.8F,5.8F,5.8F,5.8F,5.8F,5.8F,5.8F,5.8F,5.8F,5.8F,5.8F)
var Evet:Float=0.25F
var Evet_Scen= floatArrayOf(0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F,0.25F)
var Evit:Float=0.5F
var Evit_Scen= floatArrayOf(0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F)
var Eadmax:Float=10.1F
var Eadmax_Scen= floatArrayOf(10.1F,10.1F,10.1F,10.1F,10.1F,10.1F,10.1F,10.1F,10.1F,10.1F,10.1F)
var Eadmin:Float=2.26F
var Eadmin_Scen= floatArrayOf(2.26F,2.26F,2.26F,2.26F,2.26F,2.26F,2.26F,2.26F,2.26F,2.26F,2.26F)
var Evdmax:Float=34.38F
var Evdmax_Scen= floatArrayOf(34.38F,34.38F,34.38F,34.38F,34.38F,34.38F,34.38F,34.38F,34.38F,34.38F,34.38F)
var Evdmin:Float=2.62F
var Evdmin_Scen= floatArrayOf(2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F,2.62F)
var Eap:Float=10.95F
var Eap_Scen= floatArrayOf(10.95F,10.95F,10.95F,10.95F,10.95F,10.95F,10.95F,10.95F,10.95F,10.95F,10.95F)
var Evp:Float=0.48F
var Evp_Scen= floatArrayOf(0.48F,0.48F,0.48F,0.48F,0.48F,0.48F,0.48F,0.48F,0.48F,0.48F,0.48F)

var Rai:Float=0.06F
var Rai_Scen= floatArrayOf(0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F)
var Rvi:Float=0.025F
var Rvi_Scen= floatArrayOf(0.025F,0.025F,0.025F,0.025F,0.025F,0.025F,0.025F,0.025F,0.025F,0.025F,0.025F)
var Rait:Float=1.5F
var Rait_Scen= floatArrayOf(1.5F,1.5F,1.5F,1.5F,1.5F,1.5F,1.5F,1.5F,1.5F,1.5F,1.5F)
var Raet:Float=4.2F
var Raet_Scen= floatArrayOf(4.2F,4.2F,4.2F,4.2F,4.2F,4.2F,4.2F,4.2F,4.2F,4.2F,4.2F)
var Rvet:Float=0.21F
var Rvet_Scen= floatArrayOf(0.21F,0.21F,0.21F,0.21F,0.21F,0.21F,0.21F,0.21F,0.21F,0.21F,0.21F)
var Rvit:Float=0.015F
var Rvit_Scen= floatArrayOf(0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F)
var R_ad:Float=0.06F
var Rad_Scen= floatArrayOf(0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F)
var Rvd:Float=0.018F
var Rvd_Scen= floatArrayOf(0.018F,0.018F,0.018F,0.018F,0.018F,0.018F,0.018F,0.018F,0.018F,0.018F,0.018F)
var Rap:Float=0.06F
var Rap_Scen= floatArrayOf(0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F,0.06F)
var Rvp:Float=0.015F
var Rvp_Scen= floatArrayOf(0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F,0.015F)

var ConsaitO2:Float=0.32F
var ConsaitO2_Scen= floatArrayOf(0.32F,0.32F,0.32F,0.32F,0.32F,0.32F,0.32F,0.32F,0.32F,0.32F,0.32F)
var ConsaetO2:Float=0.65F
var ConsaetO2_Scen= floatArrayOf(0.65F,0.65F,0.65F,0.65F,0.65F,0.65F,0.65F,0.65F,0.65F,0.65F,0.65F)
var ConsvpO2:Float=0.03F
var ConsvpO2_Scen= floatArrayOf(0.03F,0.03F,0.03F,0.03F,0.03F,0.03F,0.03F,0.03F,0.03F,0.03F,0.03F)

var PosScene:Int=0

lateinit var avisoNoconex:TextView




open class MainActivity : AppCompatActivity(),callbackInterface {

    protected lateinit var livedataBroadcastreceiverwifi: LiveData_broadcastReceiverWifi
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contextMainActivity=this
        configView()
        val ecgFragment=
            ECGFragment()
        avisoNoconex=textViewAdv
        avisoNoconex.setText("El Simulador está desconectado")
        avisoNoconex.visibility=View.INVISIBLE

        /* Escuchador WIFI y verificador de red Simulador*/
        livedataBroadcastreceiverwifi= LiveData_broadcastReceiverWifi()

        if (!socket.isClosed){ socket.close() }

    }

    //Ciclos de vida de la aplicación
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //Desactivar Pantalla activa
        if(graficando){UDPComm().sendUDP("f@")}
        recv_on=false
        socket.close()
    }

    private fun configView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // Mantener pantalla activa
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_params, R.id.navigation_plots
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun callbackmethod(action: Int) {
        //val transaction=this.supportFragmentManager.beginTransaction()
        //Llamado de fragmentos Parámetros personalizados
        when (action){
            1->navController.navigate(R.id.action_navigation_params_to_ECGFragment)
            2->navController.navigate(R.id.action_navigation_params_to_MecResp)
            3->navController.navigate(R.id.action_navigation_params_to_sistCVFragment)
            4->navController.navigate(R.id.action_navigation_params_to_configMedScene)
        }

    }
//Retornar a fragmento anterior con flecha atrás
    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return false
    }

}
