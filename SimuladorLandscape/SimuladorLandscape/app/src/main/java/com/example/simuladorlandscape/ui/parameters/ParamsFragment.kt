package com.example.simuladorlandscape.ui.parameters

import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.simuladorlandscape.*
import com.github.mikephil.charting.utils.Utils.init
import kotlinx.android.synthetic.main.fragment_params.*
import java.net.DatagramSocket
import kotlin.math.roundToInt


class ParamsFragment : Fragment(){

    private lateinit var paramsViewModel: ParamsViewModel
    private lateinit var comunicator: callbackInterface


    var seeklistener: SeekBar.OnSeekBarChangeListener? = null

    val maxFreqCard:Float=250F     //Máximo 250 lpm
    val maxFreqResp:Float=120F
    val maxPeso:Float=5.0F
    val maxTemp:Float=42.0F
    val maxPH:Float=7.8F


    val minFreqCard:Float=30F     //Máximo 250 lpm
    val minFreqResp:Float=20F
    val minPeso:Float=2.0F
    val minTemp:Float=34.0F
    val minPH:Float=6.5F


    fun progress_valor(progress:Int): Float {
        return (progress.toFloat()/10)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        paramsViewModel =
            ViewModelProviders.of(this).get(ParamsViewModel::class.java)

        comunicator=activity as callbackInterface

       val root = inflater.inflate(com.example.simuladorlandscape.R.layout.fragment_params, container, false)
       return root

        if (switchMedScene.isChecked==true){
            spinnerMedicalScene.visibility=View.VISIBLE
            buttonConfigEM.visibility=View.VISIBLE
        }
        else{
            spinnerMedicalScene.visibility=View.INVISIBLE
            buttonConfigEM.visibility=View.INVISIBLE
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init_seekbar()
        init()
        cargar_datos_actuales()

        if (switchMedScene.isChecked==true){
            spinnerMedicalScene.visibility=View.VISIBLE
            buttonConfigEM.visibility=View.VISIBLE
        }
        else{
            spinnerMedicalScene.visibility=View.INVISIBLE
            buttonConfigEM.visibility=View.INVISIBLE
        }

        switchMedScene.setOnClickListener {
            if (switchMedScene.isChecked==true){
                spinnerMedicalScene.visibility=View.VISIBLE
                buttonConfigEM.visibility=View.VISIBLE
            }
            else{
                spinnerMedicalScene.visibility=View.INVISIBLE
                buttonConfigEM.visibility=View.INVISIBLE
            }
        }

        //Configuración spinner Escenários médicos
           spinnerMedicalScene.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (switchMedScene.isChecked==true){
                    PosScene=position
                when(position){
                    0-> { Normal()
                    Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    1-> { Isquemia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    2-> { Hiperpotasemia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    3-> { Hipopotasemia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    4-> { Hipercalcemia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    5-> { Hipocalcemia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    6-> { Hipotermia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    7-> { Taquicardia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show() }
                    8-> { Fiebre()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    9-> { Bradipnea()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    10-> { Bradicardia()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                    11-> { Taquipnea()
                        Toast.makeText(context,"Escenário Seleccionado ${parent?.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()}
                }   }
            }

        }


        //Solicitud de lectura de parámetros generales
        if (socket.isClosed){ socket= DatagramSocket(simPort)  }
        //Envío de parámetros
        UDPComm().sendUDP("a${FreqCard.toString()}@")
        UDPComm().sendUDP("b${FreqResp.toString()}@")
        UDPComm().sendUDP("c${Peso.toString()}@")
        UDPComm().sendUDP("d${Temp.toString()}@")
        UDPComm().sendUDP("e${PH.toString()}@")
        if (!graficando){ socket.close() }

        val wifiObserver=Observer<Int> { if(it==2) { mostrarBotonesParam(true)
                                                     avisoNoconex.visibility=View.INVISIBLE }
                                              else { mostrarBotonesParam(false)
                                                     avisoNoconex.visibility=View.VISIBLE } }

        paramsViewModel.wifiState.observe(viewLifecycleOwner,wifiObserver)

        var on_edit:Boolean=false
        val freqCardObserver= Observer<String> { editTextFreqCard.setText(it)  }
        val freqRespObserver= Observer<String> { editTextFreqResp.setText(it)  }
        val pesoObserver= Observer<String> { editTextPeso.setText(it)  }
        val tempObserver= Observer<String> { editTextTemp.setText(it)  }
        val pHObserver= Observer<String> { editTextPH.setText(it)  }

//Llamado de fragmentos vía interface y su función en el MainActivity
        buttonECGPuls.setOnClickListener {  comunicator.callbackmethod(1) }
        buttonMecResp.setOnClickListener {  comunicator.callbackmethod(2) }
        buttonSistCV.setOnClickListener { comunicator.callbackmethod(3) }
        buttonConfigEM.setOnClickListener { comunicator.callbackmethod(4) }

    //Escuchador seekbars
        seeklistener = object : SeekBar.OnSeekBarChangeListener {       //Escuchador cambios en SeekBars
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                var Valor:Float=0F
                if (!on_edit) {
                    when (seekBar) {
                        seekBarFreqCard -> {
                            Valor=progress_valor(seekBarFreqCard.progress)
                            if (Valor>=minFreqCard && Valor<=maxFreqCard){
                            editTextFreqCard.setText(Valor.toString())}
                            else{
                                editTextFreqCard.setText(FreqCard.toString())
                                seekBar.progress= (FreqCard*10).toInt()
                                Toast.makeText(context,"El valor debe estar entre ${minFreqCard} y ${maxFreqCard}",Toast.LENGTH_SHORT).show()
                            }
                        }
                        seekBarFreqResp -> {
                            Valor=progress_valor(seekBarFreqResp.progress)
                            if (Valor>=minFreqResp && Valor<=maxFreqResp){
                                editTextFreqResp.setText(Valor.toString())}
                            else{
                                editTextFreqResp.setText(FreqResp.toString())
                                seekBar.progress= (FreqResp*10).toInt()
                                Toast.makeText(context,"El valor debe estar entre ${minFreqResp} y ${maxFreqResp}",Toast.LENGTH_SHORT).show()
                            }
                        }
                        seekBarPeso -> {
                            Valor=progress_valor(seekBarPeso.progress)
                            if (Valor>=minPeso && Valor<=maxPeso){
                                editTextPeso.setText(Valor.toString())}
                            else{
                                editTextPeso.setText(Peso.toString())
                                seekBar.progress= (Peso*10).toInt()
                                Toast.makeText(context,"El valor debe estar entre ${minPeso} y ${maxPeso}",Toast.LENGTH_SHORT).show()
                            }
                        }
                        seekBarTemp -> {
                            Valor=progress_valor(seekBarTemp.progress)
                            if (Valor>=minTemp && Valor<=maxTemp){
                                editTextTemp.setText(Valor.toString())}
                            else{
                                editTextTemp.setText(Temp.toString())
                                seekBar.progress= (Temp*10).toInt()
                                Toast.makeText(context,"El valor debe estar entre ${minTemp} y ${maxTemp}",Toast.LENGTH_SHORT).show()
                            }
                        }
                        seekBarPH -> {
                            Valor=progress_valor(seekBarPH.progress)
                            if (Valor>=minPH && Valor<=maxPH){
                                editTextPH.setText(Valor.toString())}
                            else{
                                editTextPH.setText(PH.toString())
                                seekBar.progress= (PH*10).toInt()
                                Toast.makeText(context,"El valor debe estar entre ${minPH} y ${maxPH}",Toast.LENGTH_SHORT).show()
                            }
                        }

                    }}}

            override fun onStartTrackingTouch(seekBar: SeekBar) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        }

        fun captureDataEdtTxt(edText:EditText,max:Float,min:Float)=object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                var valcapt:String=edText.text.toString()
                var Valcapt:Int=(valcapt.toFloat()*10).toInt()
                if (actionId==EditorInfo.IME_ACTION_DONE){
                    if (valcapt.toFloat()>=min && valcapt.toFloat()<=max){
                        when (edText){  editTextFreqCard->{//FreqCard=valcapt.toFloat()
                                                            seekBarFreqCard.progress= Valcapt }
                                        editTextFreqResp->{//FreqResp=valcapt.toFloat()
                                                            seekBarFreqResp.progress= Valcapt }
                                        editTextPeso->{//Peso=valcapt.toFloat()
                                                        seekBarPeso.progress= Valcapt }
                                        editTextTemp->{//Temp=valcapt.toFloat()
                                                        seekBarTemp.progress= Valcapt }
                                        editTextPH->{//PH=valcapt.toFloat()
                                                        seekBarPH.progress= Valcapt }  }
                    }
                    else {
                        Toast.makeText(context,"El valor debe estar entre ${min} y ${max}",Toast.LENGTH_SHORT).show()
                        when (edText){  editTextFreqCard->{edText.setText(FreqCard.toString())
                                                            seekBarFreqCard.progress= (FreqCard*10).toInt()}
                                        editTextFreqResp->{edText.setText(FreqResp.toString())
                                                            seekBarFreqResp.progress= (FreqResp*10).toInt()}
                                        editTextPeso->{edText.setText(Peso.toString())
                                                        seekBarPeso.progress= (Peso*10).toInt()}
                                        editTextTemp->{edText.setText(Temp.toString())
                                                        seekBarTemp.progress= (Temp*10).toInt()}
                                        editTextPH->{edText.setText(PH.toString())
                                                        seekBarPH.progress= (PH*10).toInt()}  }
                        }
                    }
                return false
                }
            }


        fun edTFreqCardlistener(edTxt:EditText,max:Float,min:Float)=object: TextWatcher {
            var valcapt:String="0.0"
            override fun afterTextChanged(s: Editable?) {              }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                on_edit=true
                valcapt=s.toString()
                if (valcapt.isNullOrEmpty()){ valcapt = min.toString()
                                                edTxt.setText(valcapt)}
                else if (valcapt.toFloat()> max){ valcapt = max.toString()
                                                edTxt.setText(valcapt)    }
                else if (valcapt.toFloat()< min){valcapt = min.toString()
                                                edTxt.setText(valcapt)}
                when (edTxt) {
                    editTextFreqCard -> {
                        //paramsViewModel.setFreqCard(valcapt)
                        FreqCard=valcapt.toFloat()
                        seekBarFreqCard.progress=(valcapt.toFloat()*10).toInt() }
                    editTextFreqResp -> {
                        //paramsViewModel.setFreqResp(valcapt)
                        FreqResp=valcapt.toFloat()
                        seekBarFreqResp.progress=(valcapt.toFloat()*10).toInt() }
                    editTextPeso -> {
                        //paramsViewModel.setPresSyst(valcapt)
                        Peso=valcapt.toFloat()
                        seekBarPeso.progress=(valcapt.toFloat()*10).toInt() }
                    editTextTemp -> {
                        //paramsViewModel.setPresDiast(valcapt)
                        Temp=valcapt.toFloat()
                        seekBarTemp.progress=(valcapt.toFloat()*10).toInt() }
                    editTextPH -> {
                        //paramsViewModel.setSatO2(valcapt)
                        PH=valcapt.toFloat()
                        seekBarPH.progress=(valcapt.toFloat()*10).toInt() }

                }
            on_edit=false
            }
        }
        /* Enviando todos los parámetros */
        buttonSendParam.setOnClickListener {
            //Cargando los valores en las variables
            FreqCard=editTextFreqCard.text.toString().toFloat()
            FreqResp=editTextFreqResp.text.toString().toFloat()
            Peso=editTextPeso.text.toString().toFloat()
            Temp=editTextTemp.text.toString().toFloat()
            PH=editTextPH.text.toString().toFloat()
            //Enviando los valores vía Wifi
            if (socket.isClosed){ socket= DatagramSocket(simPort)}
            UDPComm().sendUDP("a${FreqCard.toString()}${(64).toChar()}")
            UDPComm().sendUDP("b${FreqResp.toString()}${(64).toChar()}")
            UDPComm().sendUDP("c${Peso.toString()}@")
            UDPComm().sendUDP("d${Temp.toString()}@")
            UDPComm().sendUDP("e${PH.toString()}@")
            if (!graficando){ socket.close() }
        }
        buttonEnv_Fc.setOnClickListener {
            FreqCard=editTextFreqCard.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort)}
            UDPComm().sendUDP("a${FreqCard.toString()}${(64).toChar()}")
            if (!graficando){ socket.close() }
            Toast.makeText(this.context,"FreqCard enviada",Toast.LENGTH_SHORT).show()
        }

        buttonEnvFr.setOnClickListener {
            FreqResp=editTextFreqResp.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort)}
            UDPComm().sendUDP("b${FreqResp.toString()}@")
            if (!graficando){ socket.close() }
            Toast.makeText(this.context,"FreqResp enviada",Toast.LENGTH_SHORT).show()
        }

        buttonEnvPeso.setOnClickListener {
            Peso=editTextPeso.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort)}
            UDPComm().sendUDP("c${Peso.toString()}@")
            if (!graficando){ socket.close() }
            Toast.makeText(this.context,"Peso enviado",Toast.LENGTH_SHORT).show()
        }

        buttonEnvTemp.setOnClickListener {
            Temp=editTextTemp.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort)}
            UDPComm().sendUDP("d${Temp.toString()}@")
            if (!graficando){ socket.close() }
            Toast.makeText(this.context,"Temperatura enviada",Toast.LENGTH_SHORT).show()
        }

        buttonEnvPH.setOnClickListener {
            PH=editTextPH.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort)}
            UDPComm().sendUDP("e${PH.toString()}@")
            if (!graficando){ socket.close() }
            Toast.makeText(this.context,"PH enviado",Toast.LENGTH_SHORT).show()
        }


        seekBarFreqCard.setOnSeekBarChangeListener(seeklistener)
        seekBarFreqResp.setOnSeekBarChangeListener(seeklistener)
        seekBarPeso.setOnSeekBarChangeListener(seeklistener)
        seekBarTemp.setOnSeekBarChangeListener(seeklistener)
        seekBarPH.setOnSeekBarChangeListener(seeklistener)


        //editTextFreqCard.addTextChangedListener(edTFreqCardlistener(editTextFreqCard,maxFreqCard,minFreqCard))
        editTextFreqCard.setOnEditorActionListener(captureDataEdtTxt(editTextFreqCard,maxFreqCard,minFreqCard))
        //editTextFreqResp.addTextChangedListener(edTFreqCardlistener(editTextFreqResp,maxFreqResp,minFreqResp))
        editTextFreqResp.setOnEditorActionListener(captureDataEdtTxt(editTextFreqResp,maxFreqResp,minFreqResp))
        //editTextPeso.addTextChangedListener(edTFreqCardlistener(editTextPeso,maxPeso,minPeso))
        editTextPeso.setOnEditorActionListener(captureDataEdtTxt(editTextPeso,maxPeso,minPeso))
        //editTextTemp.addTextChangedListener(edTFreqCardlistener(editTextTemp,maxTemp,minTemp))
        editTextTemp.setOnEditorActionListener(captureDataEdtTxt(editTextTemp,maxTemp,minTemp))
        //editTextPH.addTextChangedListener(edTFreqCardlistener(editTextPH,maxPH,minPH))
        editTextPH.setOnEditorActionListener(captureDataEdtTxt(editTextPH,maxPH,minPH))

    }

    private fun cargar_datos_actuales() {
        editTextFreqCard.setText(FreqCard.toString())
        seekBarFreqCard.progress=(FreqCard*10).toInt()
        editTextFreqResp.setText(FreqResp.toString())
        seekBarFreqResp.progress=(FreqResp*10).toInt()
        editTextPeso.setText(Peso.toString())
        seekBarPeso.progress=(Peso*10).toInt()
        editTextTemp.setText(Temp.toString())
        seekBarTemp.progress=(Temp*10).toInt()
        editTextPH.setText(PH.toString())
        seekBarPH.progress=(PH*10).toInt()
    }

    private fun mostrarBotonesParam(en:Boolean) {
        if(en){
            buttonSendParam.visibility=View.VISIBLE
            buttonEnv_Fc.visibility=View.VISIBLE
            buttonEnvFr.visibility=View.VISIBLE
            buttonEnvPeso.visibility=View.VISIBLE
            buttonEnvTemp.visibility=View.VISIBLE
            buttonEnvPH.visibility=View.VISIBLE
        }
        else {
            buttonSendParam.visibility=View.INVISIBLE
            buttonEnv_Fc.visibility=View.INVISIBLE
            buttonEnvFr.visibility=View.INVISIBLE
            buttonEnvPeso.visibility=View.INVISIBLE
            buttonEnvTemp.visibility=View.INVISIBLE
            buttonEnvPH.visibility=View.INVISIBLE
        }
    }

    private fun init_seekbar() {
        seekBarFreqCard.max= (maxFreqCard*10).toInt()
        //seekBarFreqCard.min= (minFreqCard*10).roundToInt()
        seekBarFreqCard.progress= (FreqCard*10).toInt()
        seekBarFreqResp.max= (maxFreqResp*10).roundToInt()
        //seekBarFreqResp.min= (minFreqResp*10).roundToInt()
        seekBarFreqResp.progress= (FreqResp*10).toInt()
        seekBarPeso.max= (maxPeso*10).roundToInt()
        //seekBarPeso.min= (minPeso*10).roundToInt()
        seekBarPeso.progress= (Peso*10).toInt()
        seekBarTemp.max= (maxTemp*10).toInt()
        //seekBarTemp.min= (minTemp*10).roundToInt()
        seekBarTemp.progress= (Temp*10).toInt()
        seekBarPH.max= (maxPH*10).toInt()
        //seekBarPH.min= (minPH*10).roundToInt()
        seekBarPH.progress= (PH*10).toInt()

    }
    fun init(){
        editTextFreqCard.setText(FreqCard.toString())
        editTextFreqResp.setText(FreqResp.toString())
        editTextPeso.setText(Peso.toString())
        editTextTemp.setText(Temp.toString())
        editTextPH.setText(PH.toString())

    }

    fun Normal(){
         FreqCard=130.0F
         FreqResp=80.0F
         Peso=3.4F
         Temp=37.4F
         PH=7.4F

         DesvFc=5F     //1-20
         F_lf=0.1F     //0.05-0.15
         F_hf=0.5F     //0.16-2
         Desv_lf=0.1F  //0.01-0.5
         Desv_hf=0.1F  //0.01-0.5
         LF_HF=0.5F    //0.1-50

//Parámetros ECG
         ThetP=0F      //0-2pi
         ThetQ=Math.round(pi/4*100).toFloat()/100
         ThetR=Math.round(pi/3*100).toFloat()/100
         ThetS=Math.round(5*pi/12*100).toFloat()/100
         ThetT=Math.round(5*pi/6*100).toFloat()/100

         a_P=1.2F  //-10-30
         a_Q=-5F
         a_R=30F
         a_S=-7.5F
         a_T=0.75F

         b_P=0.25F  //0-2
         b_Q=0.1F
         b_R=0.1F
         b_S=0.1F
         b_T=0.4F
//Parámetros Pulso
         ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
         ThetOR=Math.round(5*pi/4*100).toFloat()/100

         a_OI=2F  //-10-30
         a_OR=1F

         b_OI=1F  //0-2
         b_OR=1F

//Parámetros Mec. Resp
         DeltaPmax=1F
         Patm=760F
         Tinsp=0.3F
         Rresp=0.014F  //0.014-0.029
         CapPulm=65F   //65-70ml/Kg
         VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
         Eait=13.64F
         Eaet=5.8F
         Evet=0.25F
         Evit=0.5F
         Eadmax=10.1F
         Eadmin=2.26F
         Evdmax=34.38F
         Evdmin=2.62F
         Eap=10.95F
         Evp=0.48F

         Rai=0.06F
         Rvi=0.025F
         Rait=1.5F
         Raet=4.2F
         Rvet=0.21F
         Rvit=0.015F
         R_ad=0.06F
         Rvd=0.018F
         Rap=0.06F
         Rvp=0.015F

         ConsaitO2=0.32F
         ConsaetO2=0.65F
         ConsvpO2=0.03F
    }

    fun Isquemia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Hiperpotasemia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Hipopotasemia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Hipercalcemia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Hipocalcemia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Hipotermia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Taquicardia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Fiebre(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Bradipnea(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Bradicardia(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }

    fun Taquipnea(){
        FreqCard=130.0F
        FreqResp=80.0F
        Peso=3.4F
        Temp=37.4F
        PH=7.4F

        DesvFc=5F     //1-20
        F_lf=0.1F     //0.05-0.15
        F_hf=0.5F     //0.16-2
        Desv_lf=0.1F  //0.01-0.5
        Desv_hf=0.1F  //0.01-0.5
        LF_HF=0.5F    //0.1-50

//Parámetros ECG
        ThetP=0F      //0-2pi
        ThetQ=Math.round(pi/4*100).toFloat()/100
        ThetR=Math.round(pi/3*100).toFloat()/100
        ThetS=Math.round(5*pi/12*100).toFloat()/100
        ThetT=Math.round(5*pi/6*100).toFloat()/100

        a_P=1.2F  //-10-30
        a_Q=-5F
        a_R=30F
        a_S=-7.5F
        a_T=0.75F

        b_P=0.25F  //0-2
        b_Q=0.1F
        b_R=0.1F
        b_S=0.1F
        b_T=0.4F
//Parámetros Pulso
        ThetOI=Math.round(pi/2*100).toFloat()/100      //0-2pi
        ThetOR=Math.round(5*pi/4*100).toFloat()/100

        a_OI=2F  //-10-30
        a_OR=1F

        b_OI=1F  //0-2
        b_OR=1F

//Parámetros Mec. Resp
        DeltaPmax=1F
        Patm=760F
        Tinsp=0.3F
        Rresp=0.014F  //0.014-0.029
        CapPulm=65F   //65-70ml/Kg
        VD=0.3F

//Parámetros Cardiovasculares

        cHbSang=0.18F
        ConsTotO2=0.14F
        Shunt=0.01F
        IndResp=0.8F

        Eaimax=3.72F
        Eaimin=3.5F
        Evimax=53.1F
        Evimin=2.63F
        Eait=13.64F
        Eaet=5.8F
        Evet=0.25F
        Evit=0.5F
        Eadmax=10.1F
        Eadmin=2.26F
        Evdmax=34.38F
        Evdmin=2.62F
        Eap=10.95F
        Evp=0.48F

        Rai=0.06F
        Rvi=0.025F
        Rait=1.5F
        Raet=4.2F
        Rvet=0.21F
        Rvit=0.015F
        R_ad=0.06F
        Rvd=0.018F
        Rap=0.06F
        Rvp=0.015F

        ConsaitO2=0.32F
        ConsaetO2=0.65F
        ConsvpO2=0.03F
    }




}

