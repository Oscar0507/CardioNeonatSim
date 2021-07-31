package com.example.simuladorlandscape.ui.parameters.innerParameters.ECGParam

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.simuladorlandscape.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData

import kotlinx.android.synthetic.main.ecg.*
import kotlinx.android.synthetic.main.fragment_params.*
import kotlinx.android.synthetic.main.fragment_plots.editTextPH
import java.net.DatagramSocket
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt


class ECGFragment : Fragment() {

    val maxFreqCard=250F
    val minFreqCard=30F

    val minDesvFc=1F
    val maxDesvFc=20F

    val minLF_HF=0.1F
    val maxLF_HF=50F

    val minFlf=0.05F
    val maxFLf=0.15F

    val minDesvFlf=0.01F
    val maxDesvFlf=0.1F

    val minFhf=0.16F
    val maxFhf=2F

    val minDesvFhf=0.01F
    val maxDesvFhf=0.5F

    val maxAngles=2* pi
    val minAngles=0F

    val maxAmp=50F
    val minAmp=-20F

    val maxAncho=3F
    val minAncho=0.1F

    lateinit var GraficaSpec: LineChart
    lateinit var GraficaECGForm: LineChart
    lateinit var GraficaPulso: LineChart

    lateinit var SetgraphSpect:LineDataSet
    lateinit var SetgraphECG:LineDataSet
    lateinit var SetgraphPulso:LineDataSet

    lateinit var DataLineSpec:LineData
    lateinit var DataLineECG:LineData
    lateinit var DataLinePulso:LineData

    lateinit var DatosSpec:ArrayList<Entry>
    lateinit var DatosECG:ArrayList<Entry>
    lateinit var DatosPulso:ArrayList<Entry>

    private lateinit var ecgViewModel: ECGViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ecgViewModel =
            ViewModelProviders.of(this).get(ECGViewModel::class.java)
        val root = inflater.inflate(R.layout.ecg, container, false)
        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GraficaSpec=linechartEspectro
        GraficaECGForm=linechartECG
        GraficaPulso =linechartPulso

        SetgraphSpect=LineDataSet(null,"Espectro    Ejex: freq[Hz]  Ejey: Pot[s^2/Hz]")
        SetgraphECG=LineDataSet(null,"ECG   Ejex: áng[rad]   Ejey: Amp[mV]")
        SetgraphPulso=LineDataSet(null,"Pulso   Ejex: áng[rad]   Ejey: Amp[mV]")

        DataLineSpec=LineData()
        DataLineECG=LineData()
        DataLinePulso=LineData()

        DatosSpec=ArrayList<Entry>()
        DatosECG=ArrayList<Entry>()
        DatosPulso=ArrayList<Entry>()


        carga_datosECG_actuales()
        //Inicialización de graficas
        Initgraphs()


        val wifiObserver= Observer<Int> { if(it==2) { verBotonesECG(true)
                                                      avisoNoconex.visibility=View.INVISIBLE }
                                        else { verBotonesECG(false)
                                               avisoNoconex.visibility=View.VISIBLE } }

        ecgViewModel.wifiState.observe(viewLifecycleOwner,wifiObserver)

        //Eventos de Botones de graficación
        buttonGrafSpect.setOnClickListener { graficarSpectro() }
        buttonGrafECG.setOnClickListener { graficarECG() }
        buttonGrafPulso.setOnClickListener { graficarPulso() }
        //Eventos carga de Datos
        buttonEnvFc.setOnClickListener { FreqCard=editTextFc.text.toString().toFloat()
                                        if (socket.isClosed){ socket= DatagramSocket(simPort) }
                                        UDPComm().sendUDP("a${FreqCard.toString()}@")
                                        if (!graficando){ socket.close() }}
        buttonEnvDesvFc.setOnClickListener { DesvFc=editTextDesvFc.text.toString().toFloat()
                                        if (socket.isClosed){ socket= DatagramSocket(simPort) }
                                        UDPComm().sendUDP("A${DesvFc.toString()}@")
                                        if (!graficando){ socket.close() }}
        buttonEnvLF_HF.setOnClickListener { LF_HF=editText_lf_hf.text.toString().toFloat()
                                        if (socket.isClosed){ socket= DatagramSocket(simPort) }
                                        UDPComm().sendUDP("B${LF_HF.toString()}@")
                                        if (!graficando){ socket.close() }}
        buttonEnvLF.setOnClickListener { F_lf=editText_lf.text.toString().toFloat()
                                        if (socket.isClosed){ socket= DatagramSocket(simPort) }
                                        UDPComm().sendUDP("C${F_lf.toString()}@")
                                        if (!graficando){ socket.close() }}
        buttonEnvDesvLF.setOnClickListener { Desv_lf=editTextDesvLf.text.toString().toFloat()
                                        if (socket.isClosed){ socket= DatagramSocket(simPort) }
                                        UDPComm().sendUDP("D${Desv_lf.toString()}@")
                                        if (!graficando){ socket.close() }}
        buttonEnvHF.setOnClickListener { F_hf=editText_hf.text.toString().toFloat()
                                        if (socket.isClosed){ socket= DatagramSocket(simPort) }
                                        UDPComm().sendUDP("E${F_hf.toString()}@")
                                        if (!graficando){ socket.close() }}
        buttonEnvDesvHF.setOnClickListener { Desv_hf=editTextDesvHf.text.toString().toFloat()
                                        if (socket.isClosed){ socket= DatagramSocket(simPort) }
                                        UDPComm().sendUDP("F${Desv_hf.toString()}@")
                                        if (!graficando){ socket.close() }}
        buttonEnvParamECG.setOnClickListener {
            ThetP=editTextThetP.text.toString().toFloat()
            ThetQ=editTextThetQ.text.toString().toFloat()
            ThetR=editTextThetR.text.toString().toFloat()
            ThetS=editTextThetS.text.toString().toFloat()
            ThetT=editTextThetT.text.toString().toFloat()

            a_P=editTextaP.text.toString().toFloat()
            a_Q=editTextaQ.text.toString().toFloat()
            a_R=editTextaR.text.toString().toFloat()
            a_S=editTextaS.text.toString().toFloat()
            a_T=editTextaT.text.toString().toFloat()

            b_P=editTextbP.text.toString().toFloat()
            b_Q=editTextbQ.text.toString().toFloat()
            b_R=editTextbR.text.toString().toFloat()
            b_S=editTextbS.text.toString().toFloat()
            b_T=editTextbT.text.toString().toFloat()

            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("G${ThetP.toString()}@")
            UDPComm().sendUDP("H${ThetQ.toString()}@")
            UDPComm().sendUDP("I${ThetR.toString()}@")
            UDPComm().sendUDP("J${ThetS.toString()}@")
            UDPComm().sendUDP("K${ThetT.toString()}@")

            UDPComm().sendUDP("L${a_P.toString()}@")
            UDPComm().sendUDP("M${a_Q.toString()}@")
            UDPComm().sendUDP("N${a_R.toString()}@")
            UDPComm().sendUDP("Ñ${a_S.toString()}@")
            UDPComm().sendUDP("O${a_T.toString()}@")

            UDPComm().sendUDP("P${b_P.toString()}@")
            UDPComm().sendUDP("Q${b_Q.toString()}@")
            UDPComm().sendUDP("R${b_R.toString()}@")
            UDPComm().sendUDP("S${b_S.toString()}@")
            UDPComm().sendUDP("T${b_T.toString()}@")

            if (!graficando){ socket.close() }}
        buttonEnvParamPulso.setOnClickListener {
            ThetOI=editTextThetOI.text.toString().toFloat()
            ThetOR=editTextThetOR.text.toString().toFloat()

            a_OI=editTextaOI.text.toString().toFloat()
            a_OR=editTextaOR.text.toString().toFloat()

            b_OI=editTextbOI.text.toString().toFloat()
            b_OR=editTextbOR.text.toString().toFloat()

            if (socket.isClosed){ socket= DatagramSocket(simPort) }

            UDPComm().sendUDP("U${ThetOI.toString()}@")
            UDPComm().sendUDP("V${ThetOR.toString()}@")

            UDPComm().sendUDP("W${a_OI.toString()}@")
            UDPComm().sendUDP("X${a_OR.toString()}@")

            UDPComm().sendUDP("Y${b_OI.toString()}@")
            UDPComm().sendUDP("Z${b_OR.toString()}@")

            if (!graficando){ socket.close() }
        }
        buttonEnvTodosECG.setOnClickListener {
            FreqCard=editTextFc.text.toString().toFloat()
            DesvFc=editTextDesvFc.text.toString().toFloat()
            LF_HF=editText_lf_hf.text.toString().toFloat()
            F_lf=editText_lf.text.toString().toFloat()
            Desv_lf=editTextDesvLf.text.toString().toFloat()
            F_hf=editText_hf.text.toString().toFloat()
            Desv_hf=editTextDesvHf.text.toString().toFloat()

            ThetP=editTextThetP.text.toString().toFloat()
            ThetQ=editTextThetQ.text.toString().toFloat()
            ThetR=editTextThetR.text.toString().toFloat()
            ThetS=editTextThetS.text.toString().toFloat()
            ThetT=editTextThetT.text.toString().toFloat()

            a_P=editTextaP.text.toString().toFloat()
            a_Q=editTextaQ.text.toString().toFloat()
            a_R=editTextaR.text.toString().toFloat()
            a_S=editTextaS.text.toString().toFloat()
            a_T=editTextaT.text.toString().toFloat()

            b_P=editTextbP.text.toString().toFloat()
            b_Q=editTextbQ.text.toString().toFloat()
            b_R=editTextbR.text.toString().toFloat()
            b_S=editTextbS.text.toString().toFloat()
            b_T=editTextbT.text.toString().toFloat()

            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("a${FreqCard.toString()}@")
            UDPComm().sendUDP("A${DesvFc.toString()}@")
            UDPComm().sendUDP("B${LF_HF.toString()}@")
            UDPComm().sendUDP("C${F_lf.toString()}@")
            UDPComm().sendUDP("D${Desv_lf.toString()}@")
            UDPComm().sendUDP("E${F_hf.toString()}@")
            UDPComm().sendUDP("F${Desv_hf.toString()}@")
            UDPComm().sendUDP("G${ThetP.toString()}@")
            UDPComm().sendUDP("H${ThetQ.toString()}@")
            UDPComm().sendUDP("I${ThetR.toString()}@")
            UDPComm().sendUDP("J${ThetS.toString()}@")
            UDPComm().sendUDP("K${ThetT.toString()}@")
            UDPComm().sendUDP("L${a_P.toString()}@")
            UDPComm().sendUDP("M${a_Q.toString()}@")
            UDPComm().sendUDP("N${a_R.toString()}@")
            UDPComm().sendUDP("Ñ${a_S.toString()}@")
            UDPComm().sendUDP("O${a_T.toString()}@")
            UDPComm().sendUDP("P${b_P.toString()}@")
            UDPComm().sendUDP("Q${b_Q.toString()}@")
            UDPComm().sendUDP("R${b_R.toString()}@")
            UDPComm().sendUDP("S${b_S.toString()}@")
            UDPComm().sendUDP("T${b_T.toString()}@")
            UDPComm().sendUDP("U${ThetOI.toString()}@")
            UDPComm().sendUDP("V${ThetOR.toString()}@")
            UDPComm().sendUDP("W${a_OI.toString()}@")
            UDPComm().sendUDP("X${a_OR.toString()}@")
            UDPComm().sendUDP("Y${b_OI.toString()}@")
            UDPComm().sendUDP("Z${b_OR.toString()}@")

            if (!graficando){ socket.close() }
        }

        //Asignacion Listener a editText

        editTextFc.setOnEditorActionListener(captureDataEdtTxt(editTextFc,maxFreqCard,minFreqCard))
        editTextDesvFc.setOnEditorActionListener(captureDataEdtTxt(editTextDesvFc,maxDesvFc,minDesvFc))
        editText_lf_hf.setOnEditorActionListener(captureDataEdtTxt(editText_lf_hf,maxLF_HF,minLF_HF))
        editText_lf.setOnEditorActionListener(captureDataEdtTxt(editText_lf,maxFLf,minFlf))
        editTextDesvLf.setOnEditorActionListener(captureDataEdtTxt(editTextDesvLf,maxDesvFlf,minDesvFlf))
        editText_hf.setOnEditorActionListener(captureDataEdtTxt(editText_hf,maxFhf,minFhf))
        editTextDesvHf.setOnEditorActionListener(captureDataEdtTxt(editTextDesvHf,maxDesvFhf,minDesvFhf))
        editTextThetP.setOnEditorActionListener(captureDataEdtTxt(editTextThetP,maxAngles,minAngles))
        editTextThetQ.setOnEditorActionListener(captureDataEdtTxt(editTextThetQ,maxAngles,minAngles))
        editTextThetR.setOnEditorActionListener(captureDataEdtTxt(editTextThetR,maxAngles,minAngles))
        editTextThetS.setOnEditorActionListener(captureDataEdtTxt(editTextThetS,maxAngles,minAngles))
        editTextThetT.setOnEditorActionListener(captureDataEdtTxt(editTextThetT,maxAngles,minAngles))
        //editTextThetOI.addTextChangedListener(editTextlistener(editTextThetOI,maxAngles,minAngles))
        editTextThetOI.setOnEditorActionListener(captureDataEdtTxt(editTextThetOI,maxAngles,minAngles))
        editTextThetOR.setOnEditorActionListener(captureDataEdtTxt(editTextThetOR,maxAngles,minAngles))
        editTextaP.setOnEditorActionListener(captureDataEdtTxt(editTextaP,maxAmp,minAmp))
        editTextaQ.setOnEditorActionListener(captureDataEdtTxt(editTextaQ,maxAmp,minAmp))
        editTextaR.setOnEditorActionListener(captureDataEdtTxt(editTextaR,maxAmp,minAmp))
        editTextaS.setOnEditorActionListener(captureDataEdtTxt(editTextaS,maxAmp,minAmp))
        editTextaT.setOnEditorActionListener(captureDataEdtTxt(editTextaT,maxAmp,minAmp))
        editTextaOI.setOnEditorActionListener(captureDataEdtTxt(editTextaOI,maxAmp,minAmp))
        editTextaOR.setOnEditorActionListener(captureDataEdtTxt(editTextaOR,maxAmp,minAmp))
        editTextbP.setOnEditorActionListener(captureDataEdtTxt(editTextbP,maxAncho,minAncho))
        editTextbQ.setOnEditorActionListener(captureDataEdtTxt(editTextbQ,maxAncho,minAncho))
        editTextbR.setOnEditorActionListener(captureDataEdtTxt(editTextbR,maxAncho,minAncho))
        editTextbS.setOnEditorActionListener(captureDataEdtTxt(editTextbS,maxAncho,minAncho))
        editTextbT.setOnEditorActionListener(captureDataEdtTxt(editTextbT,maxAncho,minAncho))
        editTextbOI.setOnEditorActionListener(captureDataEdtTxt(editTextbOI,maxAncho,minAncho))
        editTextbOR.setOnEditorActionListener(captureDataEdtTxt(editTextbOR,maxAncho,minAncho))
    }

    private fun Initgraphs() {

        GraficaSpec.description.isEnabled=false
        GraficaECGForm.description.isEnabled=false
        GraficaPulso.description.isEnabled=false
        GraficaSpec.setVisibleXRangeMaximum(Tmax.toFloat())
        GraficaSpec.setTouchEnabled(true)
        GraficaSpec.setScaleEnabled(true)
        GraficaSpec.setDrawGridBackground(false)
        GraficaSpec.setPinchZoom(false)
        GraficaSpec.setBorderColor(Color.BLACK)
        GraficaSpec.setBackgroundColor(Color.TRANSPARENT)



        var EjeY_R: YAxis =GraficaSpec.axisRight
        EjeY_R.setDrawLabels(false)     //Deshabilita eje derecho
        var EjeY: YAxis =GraficaSpec.axisLeft
        //EjeY.resetAxisMinimum()
        //EjeY.resetAxisMaximum()
        //EjeY.axisMaximum=0.2F
        //EjeY.axisMinimum=0F

        var EjeY_R1: YAxis =GraficaECGForm.axisRight
        EjeY_R1.setDrawLabels(false)     //Deshabilita eje derecho
        var EjeY1: YAxis =GraficaECGForm.axisLeft

        var EjeY_R2: YAxis =GraficaPulso.axisRight
        EjeY_R2.setDrawLabels(false)     //Deshabilita eje derecho
        var EjeY2: YAxis =GraficaPulso.axisLeft


        var Ejex: XAxis =GraficaSpec.xAxis
        Ejex.position= XAxis.XAxisPosition.BOTTOM
        var Ejex1: XAxis =GraficaECGForm.xAxis
        Ejex1.position= XAxis.XAxisPosition.BOTTOM
        var Ejex2: XAxis =GraficaPulso.xAxis
        Ejex2.position= XAxis.XAxisPosition.BOTTOM



        SetgraphSpect.axisDependency=(YAxis.AxisDependency.LEFT)
        SetgraphSpect.lineWidth=3f
        SetgraphSpect.color= Color.BLACK
        SetgraphSpect.mode= LineDataSet.Mode.LINEAR
        SetgraphSpect.setDrawCircles(false)


        SetgraphECG.axisDependency=(YAxis.AxisDependency.LEFT)
        SetgraphECG.lineWidth=3f
        SetgraphECG.color= Color.RED
        SetgraphECG.mode= LineDataSet.Mode.LINEAR
        SetgraphECG.setDrawCircles(false)

        SetgraphPulso.axisDependency=(YAxis.AxisDependency.LEFT)
        SetgraphPulso.lineWidth=3f
        SetgraphPulso.color= Color.CYAN
        SetgraphPulso.mode= LineDataSet.Mode.LINEAR
        SetgraphPulso.setDrawCircles(false)

        DataLineSpec.addDataSet(SetgraphSpect)
        DataLineECG.addDataSet(SetgraphECG)
        DataLinePulso.addDataSet(SetgraphPulso)

        GraficaSpec.data=DataLineSpec
        GraficaECGForm.data=DataLineECG
        GraficaPulso.data=DataLinePulso

        graficarSpectro()
        graficarECG()
        graficarPulso()


    }

    private fun graficarSpectro() {
        var f:Float=0F
        var Sig2:Float=0.021F/(LF_HF+1)
        var Sig1:Float=Sig2* LF_HF
        var A1:Float=Sig1/(Desv_lf* sqrt(2* pi))
        var A2:Float=Sig2/(Desv_hf* sqrt(2* pi))
        var Spect:Float=0F

        DatosSpec.clear()


        for (k in 0..((F_hf+ Desv_hf+0.5)*1000).toInt()){
            f=k.toFloat()/1000
            Spect=(A1* exp(-0.5* ((f - F_lf) / Desv_lf).pow(2))).toFloat()+(A2* exp(-0.5* ((f - F_hf) / Desv_hf).pow(2))).toFloat()
            DatosSpec.add(k,Entry(f,Spect))
        }
        SetgraphSpect.values=DatosSpec
        DataLineSpec=LineData(SetgraphSpect)
        GraficaSpec.data=DataLineSpec
        GraficaSpec.invalidate()
    }

    private fun graficarECG() {
        var Thet:Float=0F
        var Dz:Float=0F
        var z:Float=0F
        var dt:Float= 1.toFloat()/1000
        var DeltaThet:Float=0F
        var a_i:Float=0F
        var b_i:Float=0F

        var ThetP_g=editTextThetP.text.toString().toFloat()
        var ThetQ_g=editTextThetQ.text.toString().toFloat()
        var ThetR_g=editTextThetR.text.toString().toFloat()
        var ThetS_g=editTextThetS.text.toString().toFloat()
        var ThetT_g=editTextThetT.text.toString().toFloat()

        var a_P_g=editTextaP.text.toString().toFloat()
        var a_Q_g=editTextaQ.text.toString().toFloat()
        var a_R_g=editTextaR.text.toString().toFloat()
        var a_S_g=editTextaS.text.toString().toFloat()
        var a_T_g=editTextaT.text.toString().toFloat()

        var b_P_g=editTextbP.text.toString().toFloat()
        var b_Q_g=editTextbQ.text.toString().toFloat()
        var b_R_g=editTextbR.text.toString().toFloat()
        var b_S_g=editTextbS.text.toString().toFloat()
        var b_T_g=editTextbT.text.toString().toFloat()

        DatosECG.clear()

        //for (k1 in 1..2){
            for (k in 0..1000){
                Thet=k.toFloat()/1000*2* pi
                z=Dz*dt+z       //Integral de Dz
                if (Thet<ThetQ_g){DeltaThet=Thet-ThetP_g
                    a_i=a_P_g
                    b_i=b_P_g   }
                else if (Thet<ThetR_g){DeltaThet=Thet-ThetQ_g
                    a_i=a_Q_g
                    b_i=b_Q_g   }
                else if (Thet<ThetS_g){DeltaThet=Thet-ThetR_g
                    a_i=a_R_g
                    b_i=b_R_g   }
                else if (Thet<ThetT_g){DeltaThet=Thet-ThetS_g
                    a_i=a_S_g
                    b_i=b_S_g   }
                else {  DeltaThet=Thet-ThetT_g
                    a_i=a_T_g
                    b_i=b_T_g   }

                if (DeltaThet<0){DeltaThet=DeltaThet+2*pi}

                Dz=(a_i*DeltaThet* exp(-0.5* ((DeltaThet) / b_i).pow(2))).toFloat()-z
                //if (k1==2){DataLineECG.addEntry(Entry(Thet,Dz),0)}
                DatosECG.add(k,Entry(Thet,Dz))
                //DataLineECG.addEntry(Entry(Thet,Dz),0)
            }
    //}
        SetgraphECG.values=DatosECG
        DataLineECG=LineData(SetgraphECG)
        GraficaECGForm.data=DataLineECG
        GraficaECGForm.invalidate()
    }

    private fun graficarPulso() {

        var Thet:Float=0F
        var Dz:Float=0F
        var z:Float=0F
        var dt:Float= 1.toFloat()/1000
        var DeltaThet:Float=0F
        var a_i:Float=0F
        var b_i:Float=0F

        var ThetOI_g=editTextThetOI.text.toString().toFloat()
        var ThetOR_g=editTextThetOR.text.toString().toFloat()
        var a_OI_g=editTextaOI.text.toString().toFloat()
        var a_OR_g=editTextaOR.text.toString().toFloat()
        var b_OI_g=editTextbOI.text.toString().toFloat()
        var b_OR_g=editTextbOR.text.toString().toFloat()

        DatosPulso.clear()

        for (k1 in 1..5){

            for (k in 0..1000){
                Thet=k.toFloat()/1000*2* pi
                z=Dz*dt+z       //Integral de Dz
                if (Thet>=ThetOI_g&&Thet<ThetOR_g){ DeltaThet=Thet-ThetOI_g
                                                a_i=a_OI_g
                                                b_i=b_OI_g }
                else {  DeltaThet=Thet-ThetOR_g
                        a_i=a_OR_g
                        b_i=b_OR_g  }

                if (DeltaThet<0){DeltaThet=DeltaThet+2*pi}

                Dz=(a_i*DeltaThet*exp(-0.5* (DeltaThet/b_i).pow(2))).toFloat()-z
                //DataLinePulso.addEntry(Entry(Thet,z),0)
                if (k1==5){//DataLinePulso.addEntry(Entry(Thet,z),0)
                    DatosPulso.add(k,Entry(Thet,z))
                     }
            }}
        SetgraphPulso.values=DatosPulso
        DataLinePulso=LineData(SetgraphPulso)
        GraficaPulso.data=DataLinePulso
        GraficaPulso.invalidate()

    }

    private fun verBotonesECG(en:Boolean) {
        if (en){
            buttonEnvFc.visibility=View.VISIBLE
            buttonEnvDesvFc.visibility=View.VISIBLE
            buttonEnvLF_HF.visibility=View.VISIBLE
            buttonEnvLF.visibility=View.VISIBLE
            buttonEnvHF.visibility=View.VISIBLE
            buttonEnvDesvHF.visibility=View.VISIBLE
            buttonEnvDesvLF.visibility=View.VISIBLE
            buttonEnvTodosECG.visibility=View.VISIBLE
            buttonEnvParamECG.visibility=View.VISIBLE
            buttonEnvParamPulso.visibility=View.VISIBLE
        }
        else{
            buttonEnvFc.visibility=View.INVISIBLE
            buttonEnvDesvFc.visibility=View.INVISIBLE
            buttonEnvLF_HF.visibility=View.INVISIBLE
            buttonEnvLF.visibility=View.INVISIBLE
            buttonEnvHF.visibility=View.INVISIBLE
            buttonEnvDesvHF.visibility=View.INVISIBLE
            buttonEnvDesvLF.visibility=View.INVISIBLE
            buttonEnvTodosECG.visibility=View.INVISIBLE
            buttonEnvParamECG.visibility=View.INVISIBLE
            buttonEnvParamPulso.visibility=View.INVISIBLE
        }
    }

    private fun carga_datosECG_actuales() {
        editTextFc.setText(FreqCard.toString())
        editTextFc_Hz.setText((Math.round(FreqCard/60*100).toFloat()/100).toString())
        editTextDesvFc.setText(DesvFc.toString())
        editText_lf_hf.setText(LF_HF.toString())
        editText_lf.setText(F_lf.toString())
        editText_hf.setText(F_hf.toString())
        editTextDesvHf.setText(Desv_hf.toString())
        editTextDesvLf.setText(Desv_lf.toString())

        editTextThetP.setText(ThetP.toString())
        editTextThetQ.setText(ThetQ.toString())
        editTextThetR.setText(ThetR.toString())
        editTextThetS.setText(ThetS.toString())
        editTextThetT.setText(ThetT.toString())

        editTextaP.setText(a_P.toString())
        editTextaQ.setText(a_Q.toString())
        editTextaR.setText(a_R.toString())
        editTextaS.setText(a_S.toString())
        editTextaT.setText(a_T.toString())

        editTextbP.setText(b_P.toString())
        editTextbQ.setText(b_Q.toString())
        editTextbR.setText(b_R.toString())
        editTextbS.setText(b_S.toString())
        editTextbT.setText(b_T.toString())

        editTextThetOI.setText(ThetOI.toString())
        editTextThetOR.setText(ThetOR.toString())

        editTextaOI.setText(a_OI.toString())
        editTextaOR.setText(a_OR.toString())

        editTextbOI.setText(b_OI.toString())
        editTextbOR.setText(b_OR.toString())
    }

    fun captureDataEdtTxt(edText:EditText,max:Float,min:Float)=object: TextView.OnEditorActionListener{
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            var valcapt:String=edText.text.toString()
            if (actionId== EditorInfo.IME_ACTION_DONE){
                if (valcapt.toFloat()>=min&&valcapt.toFloat()<=max){
                    when(edText){
                        editTextFc->{editTextFc_Hz.setText((valcapt.toFloat()/60).toString())}
                    }
                }
                else {
                    Toast.makeText(context,"El valor debe estar entre ${min} y ${max}", Toast.LENGTH_SHORT).show()
                    when (edText){
                        editTextFc->{edText.setText(FreqCard.toString())}
                        editTextDesvFc->{edText.setText(DesvFc.toString())}
                        editText_lf_hf->{edText.setText(LF_HF.toString())}
                        editText_lf->{edText.setText(F_lf.toString())}
                        editTextDesvLf->{edText.setText(Desv_lf.toString())}
                        editText_hf->{edText.setText(F_hf.toString())}
                        editTextDesvHf->{edText.setText(Desv_hf.toString())}

                        editTextThetP->{editTextThetP.setText(ThetP.toString())}
                        editTextThetQ->{editTextThetQ.setText(ThetQ.toString())}
                        editTextThetR->{editTextThetR.setText(ThetR.toString())}
                        editTextThetS->{editTextThetS.setText(ThetS.toString())}
                        editTextThetT->{editTextThetT.setText(ThetT.toString())}

                        editTextaP->{editTextaP.setText(a_P.toString())}
                        editTextaQ->{editTextaQ.setText(a_Q.toString())}
                        editTextaR->{editTextaR.setText(a_R.toString())}
                        editTextaS->{editTextaS.setText(a_S.toString())}
                        editTextaT->{editTextaT.setText(a_T.toString())}

                        editTextbP->{editTextbP.setText(b_P.toString())}
                        editTextbQ->{editTextbQ.setText(b_Q.toString())}
                        editTextbR->{editTextbR.setText(b_R.toString())}
                        editTextbS->{editTextbS.setText(b_S.toString())}
                        editTextbT->{editTextbT.setText(b_T.toString())}

                        editTextThetOI->{editTextThetOI.setText(ThetOI.toString())}
                        editTextThetOR->{editTextThetOR.setText(ThetOR.toString())}

                        editTextaOI->{editTextaOI.setText(a_OI.toString())}
                        editTextaOR->{editTextaOR.setText(a_OR.toString())}

                        editTextbOI->{editTextbOI.setText(b_OI.toString())}
                        editTextbOR->{editTextbOR.setText(b_OR.toString())}
                    }
                }
            }
            return false
        }
    }

    fun editTextlistener(edTxt: EditText, max:Float, min:Float)=object: TextWatcher {
        var valcapt:String="0.0"
        override fun afterTextChanged(s: Editable?) {              }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            var on_edit=true
            valcapt=s.toString()
            if (valcapt.isNullOrEmpty()){ valcapt = min.toString()
                edTxt.setText(valcapt)}
            else if (valcapt.toFloat()> max){ valcapt = max.toString()
                edTxt.setText(valcapt)    }
            else if (valcapt.toFloat()< min){valcapt = min.toString()
                edTxt.setText(valcapt)}
            when (edTxt) {
                editTextFc -> { FreqCard=valcapt.toFloat() }
                editTextDesvFc -> { DesvFc=valcapt.toFloat() }
                editText_lf_hf -> { LF_HF=valcapt.toFloat() }
                editText_lf -> { F_lf=valcapt.toFloat() }
                editTextDesvLf -> { Desv_lf=valcapt.toFloat()}
                editText_hf -> { F_hf=valcapt.toFloat()}
                editTextDesvHf -> { Desv_hf=valcapt.toFloat()}
                //Ciclos ECG
                editTextThetP->{ ThetP=valcapt.toFloat()}
                editTextThetQ->{ ThetQ=valcapt.toFloat()}
                editTextThetR->{ ThetR=valcapt.toFloat()}
                editTextThetS->{ ThetS=valcapt.toFloat()}
                editTextThetT->{ ThetT=valcapt.toFloat()}

                editTextaP->{ a_P=valcapt.toFloat()}
                editTextaQ->{ a_Q=valcapt.toFloat()}
                editTextaR->{ a_R=valcapt.toFloat()}
                editTextaS->{ a_S=valcapt.toFloat()}
                editTextaT->{ a_T=valcapt.toFloat()}

                editTextbP->{ b_P=valcapt.toFloat()}
                editTextbQ->{ b_Q=valcapt.toFloat()}
                editTextbR->{ b_R=valcapt.toFloat()}
                editTextbS->{ b_S=valcapt.toFloat()}
                editTextbT->{ b_T=valcapt.toFloat()}
                //Ciclos Pulso
                editTextThetOI->{ ThetOI=valcapt.toFloat()}
                editTextThetOR->{ ThetOR=valcapt.toFloat()}

                editTextaOI->{ a_OI=valcapt.toFloat()}
                editTextaOI->{ a_OR=valcapt.toFloat()}

                editTextbOI->{ b_OI=valcapt.toFloat()}
                editTextbOR->{ b_OR=valcapt.toFloat()}

            }
            on_edit=false
        }
    }


}
