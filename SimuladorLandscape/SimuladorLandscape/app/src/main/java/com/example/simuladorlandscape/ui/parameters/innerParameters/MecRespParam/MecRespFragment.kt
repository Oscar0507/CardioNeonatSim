package com.example.simuladorlandscape.ui.parameters.innerParameters.MecRespParam


import android.graphics.Color
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_mec_resp.*
import java.net.DatagramSocket

/**
 * A simple [Fragment] subclass.
 */
class MecRespFragment : Fragment() {

    val maxFreqResp:Float=120F
    val minFreqResp:Float=20F

    val maxDPmax=3F
    val minDPmax=0.5F

    val maxPatm=760F
    val minPatm=540F

    val maxTinsp=0.6F
    val minTinsp=0.2F

    val maxRresp=0.030F
    val minRresp=0.013F

    val maxCapPulm=70F
    val minCapPulm=60F

    val maxVD=0.5F
    val minVD=0.2F

    private lateinit var MecRecpView:MecRespViewModel
    private lateinit var GraficaMecResp:LineChart
    private lateinit var LineaDataSetVolResp:LineDataSet
    private lateinit var LineaDataSetPresResp:LineDataSet
    private lateinit var LineaDataMecResp:LineData
    private lateinit var DatosPresMecResp:ArrayList<Entry>
    private lateinit var DatosVolMecResp:ArrayList<Entry>

    var FrRespHz:Float=(Math.round(FreqResp/60*100).toFloat()/100)
    var Tresp:Float=Math.round(100/FrRespHz).toFloat()/100
    var tinsp:Float= Tinsp*Tresp
    var tesp:Float=Tresp-tinsp
    var VTmax:Float= 2*tinsp/(pi* Rresp)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        MecRecpView =
            ViewModelProviders.of(this).get(MecRespViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mec_resp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wifiObserver= Observer<Int> { if(it==2) { MostrarBotonesMecResp(true)
                                                      avisoNoconex.visibility=View.INVISIBLE }
                                        else { MostrarBotonesMecResp(false)
                                               avisoNoconex.visibility=View.VISIBLE } }
        MecRecpView.wifiState.observe(viewLifecycleOwner,wifiObserver)

        carga_datosMecResp_actuales()
        InitGraph()

        //Boton Graficar
        buttonGrafMecResp.setOnClickListener {
            graficarVol_Pres()
        }

        //Asignacion Listener a editText
        //editTextFR.addTextChangedListener(editTextlistener(editTextFR,maxFreqResp,minFreqResp))
        editTextFR.setOnEditorActionListener(captureDataEdtTxt(editTextFR,maxFreqResp,minFreqResp))
        editTextDPmax.setOnEditorActionListener(captureDataEdtTxt(editTextDPmax,maxDPmax,minDPmax))
        editTextPatm.setOnEditorActionListener(captureDataEdtTxt(editTextPatm,maxPatm,minPatm))
        editTextTinsp.setOnEditorActionListener(captureDataEdtTxt(editTextTinsp,maxTinsp,minTinsp))
        editTextRresp.setOnEditorActionListener(captureDataEdtTxt(editTextRresp,maxRresp,minRresp))
        editTextCapPulm.setOnEditorActionListener(captureDataEdtTxt(editTextCapPulm,maxCapPulm,minCapPulm))
        editTextV_D.setOnEditorActionListener(captureDataEdtTxt(editTextV_D,maxVD,minVD))

        buttonEnvTodosMecResp.setOnClickListener {
            FreqResp=editTextFR.text.toString().toFloat()
            DeltaPmax=editTextDPmax.text.toString().toFloat()
            Patm=editTextPatm.text.toString().toFloat()
            Tinsp=editTextTinsp.text.toString().toFloat()
            Rresp=editTextRresp.text.toString().toFloat()
            CapPulm=editTextCapPulm.text.toString().toFloat()
            VD=editTextV_D.text.toString().toFloat()

            if (socket.isClosed){ socket= DatagramSocket(simPort) }
                UDPComm().sendUDP("b${FreqResp.toString()}@")
                UDPComm().sendUDP("g${DeltaPmax.toString()}@")
                UDPComm().sendUDP("h${Patm.toString()}@")
                UDPComm().sendUDP("j${Tinsp.toString()}@")
                UDPComm().sendUDP("k${Rresp.toString()}@")
                UDPComm().sendUDP("l${CapPulm.toString()}@")
                UDPComm().sendUDP("m${VD.toString()}@")
            if (!graficando){ socket.close() }
        }

        buttonEnvFResp.setOnClickListener {
            FreqResp=editTextFR.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("b${FreqResp.toString()}@")
            if (!graficando){ socket.close() }  }
        buttonEnvDPmax.setOnClickListener {
            DeltaPmax=editTextDPmax.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("g${DeltaPmax.toString()}@")
            if (!graficando){ socket.close() }  }
        buttonEnvPatm.setOnClickListener {
            Patm=editTextPatm.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("h${Patm.toString()}@")
            if (!graficando){ socket.close() }  }
        buttonEnvTinsp.setOnClickListener {
            Tinsp=editTextTinsp.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("j${Tinsp.toString()}@")
            if (!graficando){ socket.close() }  }
        buttonEnvRresp.setOnClickListener {
            Rresp=editTextRresp.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("k${Rresp.toString()}@")
            if (!graficando){ socket.close() }  }
        buttonEnvCapPulm.setOnClickListener {
            CapPulm=editTextCapPulm.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("l${CapPulm.toString()}@")
            if (!graficando){ socket.close() }  }
        buttonEnvV_D.setOnClickListener {
            VD=editTextV_D.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("m${VD.toString()}@")
            if (!graficando){ socket.close() }  }
    }

    private fun InitGraph() {
        GraficaMecResp=linechartMecResp
        LineaDataSetVolResp= LineDataSet(null,"VTidal")
        LineaDataSetPresResp= LineDataSet(null,"ΔP_Alv")
        LineaDataMecResp= LineData()
        DatosPresMecResp=ArrayList<Entry>()
        DatosVolMecResp=ArrayList<Entry>()

        LineaDataSetVolResp.axisDependency=YAxis.AxisDependency.LEFT
        LineaDataSetVolResp.lineWidth=3f
        LineaDataSetVolResp.color= Color.BLUE
        LineaDataSetVolResp.mode= LineDataSet.Mode.LINEAR
        LineaDataSetVolResp.setDrawCircles(false)


        LineaDataSetPresResp.axisDependency=YAxis.AxisDependency.RIGHT
        LineaDataSetPresResp.lineWidth=3f
        LineaDataSetPresResp.color= Color.RED
        LineaDataSetPresResp.mode= LineDataSet.Mode.LINEAR
        LineaDataSetPresResp.setDrawCircles(false)

        var Ejex: XAxis =GraficaMecResp.xAxis
        Ejex.position= XAxis.XAxisPosition.BOTTOM

        GraficaMecResp.description.isEnabled=false
        GraficaMecResp.setBorderColor(Color.BLACK)
        GraficaMecResp.setBackgroundColor(Color.TRANSPARENT)
        LineaDataMecResp.addDataSet(LineaDataSetVolResp)
        LineaDataMecResp.addDataSet(LineaDataSetPresResp)

        GraficaMecResp.data=LineaDataMecResp


        graficarVol_Pres()
    }

    private fun graficarVol_Pres() {
        var t:Float=0F
        var VT:Float= CapPulm/2-VTmax/2
        var DVT:Float=0F
        var DeltaP:Float=0F

        var DeltaPmax_g=editTextDPmax.text.toString().toFloat()
        var FreqResp_g=editTextFR.text.toString().toFloat()
        var Tresp_g=60/FreqResp_g
        var Tinsp_g=editTextTinsp.text.toString().toFloat()
        var tinsp_g=Tresp_g*Tinsp_g
        var tesp_g=Tresp_g-tinsp_g
        var Rresp_g=editTextRresp.text.toString().toFloat()

        DatosPresMecResp.clear()
        DatosVolMecResp.clear()
        for (k in 0..1000){
            t=k*Tresp_g/1000
            VT=DVT*1/1000+VT        //Integral del Volumen corriente
            if (t<=tinsp_g){  DeltaP= -DeltaPmax_g*Math.sin((pi/tinsp_g*t).toDouble()).toFloat() }
            else{ DeltaP= DeltaPmax_g*tinsp_g/tesp_g*Math.sin((pi/tesp_g*(t-tinsp_g)).toDouble()).toFloat() }

            DVT=-DeltaP/ Rresp_g
            DatosPresMecResp.add(k,Entry(t,DeltaP))
            DatosVolMecResp.add(k,Entry(t,VT))

            LineaDataSetPresResp.values=DatosPresMecResp
            LineaDataSetPresResp.notifyDataSetChanged()
            LineaDataSetVolResp.values=DatosVolMecResp
            LineaDataSetVolResp.notifyDataSetChanged()
            LineaDataMecResp.notifyDataChanged()

            GraficaMecResp.data=LineaDataMecResp

            GraficaMecResp.invalidate()

         }



    }

    private fun MostrarBotonesMecResp(en:Boolean) {
        if (en){
            buttonEnvTodosMecResp.visibility=View.VISIBLE
            buttonEnvFResp.visibility=View.VISIBLE
            buttonEnvDPmax.visibility=View.VISIBLE
            buttonEnvPatm.visibility=View.VISIBLE
            buttonEnvTinsp.visibility=View.VISIBLE
            buttonEnvRresp.visibility=View.VISIBLE
            buttonEnvCapPulm.visibility=View.VISIBLE
            buttonEnvV_D.visibility=View.VISIBLE}
        else{
            buttonEnvTodosMecResp.visibility=View.INVISIBLE
            buttonEnvFResp.visibility=View.INVISIBLE
            buttonEnvDPmax.visibility=View.INVISIBLE
            buttonEnvPatm.visibility=View.INVISIBLE
            buttonEnvTinsp.visibility=View.INVISIBLE
            buttonEnvRresp.visibility=View.INVISIBLE
            buttonEnvCapPulm.visibility=View.INVISIBLE
            buttonEnvV_D.visibility=View.INVISIBLE
        }
    }

    private fun carga_datosMecResp_actuales() {
        editTextFR.setText(FreqResp.toString())
        editTextFr_Hz.setText((Math.round(FreqResp/60*100).toFloat()/100).toString())
        editTextTresp.setText((1/(Math.round(FreqResp/60*100).toFloat()/100)).toString())
        editTextDPmax.setText(DeltaPmax.toString())
        editTextCapPulm.setText(CapPulm.toString())
        editTextRresp.setText(Rresp.toString())
        editTextTinsp.setText(Tinsp.toString())
        editTextPatm.setText(Patm.toString())
        editTextV_D.setText(VD.toString())
        tinsp= Tinsp*Tresp
        VTmax=2*tinsp/(pi* Rresp)

    }

    fun captureDataEdtTxt(edText:EditText,max:Float,min:Float)=object: TextView.OnEditorActionListener{
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            var valcapt:String=edText.text.toString()
            if (actionId== EditorInfo.IME_ACTION_DONE){
                if (valcapt.toFloat()>=min&&valcapt.toFloat()<=max){
                    when(edText){
                        editTextFR->{   editTextFr_Hz.setText((valcapt.toFloat()/60).toString())
                                        editTextTresp.setText((60/valcapt.toFloat()).toString())    }   //Cálculo F_Hz, Tresp
                    }
                }

                else {
                    Toast.makeText(context,"El valor debe estar entre ${min} y ${max}", Toast.LENGTH_SHORT).show()
                    when (edText){
                        editTextFR->{edText.setText(FreqResp.toString())}
                        editTextDPmax->{ edText.setText(DeltaPmax.toString())}
                        editTextPatm->{ edText.setText(Patm.toString())}
                        editTextTinsp->{ edText.setText(Tinsp.toString())}
                        editTextRresp->{ edText.setText(Rresp.toString())}
                        editTextCapPulm->{ edText.setText(CapPulm.toString())}
                        editTextV_D->{ edText.setText(VD.toString())}}
                }
            }
            return false
        }
    }

    fun editTextlistener(edTxt: EditText, max:Float, min:Float)=object: TextWatcher {
        var valcapt:String="0.0"
        override fun afterTextChanged(s: Editable?) {
            var on_edit=true
            valcapt=s.toString()
            if (valcapt.isNullOrEmpty()){ valcapt = min.toString()
                edTxt.setText(valcapt)}
            else if (valcapt.toFloat()> max){ valcapt = max.toString()
                edTxt.setText(valcapt)    }
            else if (valcapt.toFloat()< min){valcapt = min.toString()
                edTxt.setText(valcapt)}
            when (edTxt) {
                editTextFR -> {  FreqResp=valcapt.toFloat()
                    FrRespHz=(Math.round(FreqResp/60*100).toFloat()/100)
                    editTextFr_Hz.setText(FrRespHz.toString())
                    Tresp=Math.round(100/FrRespHz).toFloat()/100
                    editTextTresp.setText(Tresp.toString()) }
                editTextDPmax -> { DeltaPmax=valcapt.toFloat() }
                editTextTinsp-> { Tinsp=valcapt.toFloat()
                                  tinsp= Tinsp*Tresp
                                  tesp=Tresp-tinsp  }
                editTextRresp -> { Rresp=valcapt.toFloat() }
                editTextCapPulm -> { CapPulm=valcapt.toFloat()}
            }
            on_edit=false
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

}
