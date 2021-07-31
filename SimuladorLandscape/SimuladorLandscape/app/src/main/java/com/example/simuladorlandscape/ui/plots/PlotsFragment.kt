package com.example.simuladorlandscape.ui.plots

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.simuladorlandscape.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_plots.*
import java.net.DatagramSocket

lateinit var Grafica1:LineChart
lateinit var SetECG: LineDataSet
lateinit var SetPulse: LineDataSet
lateinit var SetVolResp: LineDataSet
lateinit var SetPresion: LineDataSet
lateinit var SetSatO: LineDataSet
lateinit var SetPCO2: LineDataSet
//lateinit var SetIntRR: LineDataSet
var Data_line: LineData = LineData()
lateinit var DatoECG:Entry
lateinit var DatoVolResp:Entry
lateinit var DatoPulse:Entry
lateinit var DatoPress:Entry
lateinit var DatoSatO:Entry
lateinit var DatoPCO2:Entry
var sizeLista:Int=0
//lateinit var DatoIntRR:Entry


var seeklistenerplot: SeekBar.OnSeekBarChangeListener? = null
lateinit var DispFrCardV:TextView
lateinit var DispFrVasc:TextView
lateinit var DispFrResp:TextView
lateinit var DispPresMed:TextView
lateinit var DispPresSist:TextView
lateinit var DispPresDiast:TextView
lateinit var DispSatO:TextView
lateinit var DispPCO2:TextView

class PlotsFragment : Fragment() {

    private lateinit var plotsViewModel: PlotsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        plotsViewModel =
            ViewModelProviders.of(this).get(PlotsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_plots, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DispFrCardV=editTextFrCard
        DispFrVasc=editTextFrVasc
        DispFrResp=editTextFrResp
        DispPresMed=editTextArtPress
        DispPresSist=editTextSysPress
        DispPresDiast=editTextDiasPress
        DispSatO=editTextPH
        DispPCO2=editTextCO2

        //Verificando el botón graficar si está activado
        if (graficando){buttongraph.setText("DETENER")}
        else{buttongraph.setText("GRAFICAR")}

        seekBartgraf.max=8

        seeklistenerplot = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBarplot: SeekBar?, progress: Int, fromUser: Boolean) {
                when (seekBarplot) {
                    seekBarplotFrecCard->{
                        FreqCard=seekBarplotFrecCard.progress.toFloat()/10

                    }
                    seekBarplotFrecResp->{
                        FreqResp=seekBarplotFrecResp.progress.toFloat()/10

                    }
                    seekBartgraf->{
                        if (seekBartgraf.progress>0){
                        Tmax=seekBartgraf.progress}
                        else{
                            Tmax=1
                            seekBartgraf.progress= Tmax
                        }
                    }
                }
            }
            override fun onStartTrackingTouch(seekBarplot: SeekBar?) {}
            override fun onStopTrackingTouch(seekBarplot: SeekBar?) {

                when (seekBarplot) {
                    seekBarplotFrecCard->{
                        textViewFCard.setText(FreqCard.toString())
                        UDPComm().sendUDP("a${FreqCard.toString()}${(64).toChar()}")
                    }
                    seekBarplotFrecResp->{
                        textViewFResp.setText(FreqResp.toString())
                        UDPComm().sendUDP("b${FreqResp.toString()}${(64).toChar()}")

                    }
                    seekBartgraf->{
                        TextViewTmax.setText(Tmax.toString())
                        Grafica1.setVisibleXRange(0f, Tmax.toFloat())
                    }
                }

            }
        }

        seekBarplotFrecResp.setOnSeekBarChangeListener(seeklistenerplot)
        seekBarplotFrecCard.setOnSeekBarChangeListener(seeklistenerplot)
        seekBartgraf.setOnSeekBarChangeListener(seeklistenerplot)

        switchModificar.setOnCheckedChangeListener { buttonView, isChecked ->
            visible_on(isChecked)
        }

       visible_on(switchModificar.isChecked)
        init_graph()

        val wifiObserver= Observer<Int> {Wifistate->
            if(Wifistate==2){
                buttongraph.visibility=View.VISIBLE
                switchModificar.visibility=View.VISIBLE
                avisoNoconex.visibility=View.INVISIBLE }
            else{ buttongraph.visibility=View.INVISIBLE
                switchModificar.visibility=View.INVISIBLE
                avisoNoconex.visibility=View.VISIBLE
                }
    }
        plotsViewModel.wifiState.observe(viewLifecycleOwner,wifiObserver)

        /*Evento de botón graficar..*/
        buttongraph.setOnClickListener {
            if (!graficando){
                graficando=true
                if(socket.isClosed) socket= DatagramSocket(simPort)
                recv_on=true
                UDPReceiverThread = Thread(UDPComm.UDPReceiver())
                UDPReceiverThread.start()
                UDPComm().sendUDP("i${(13).toChar()}")          //${13.toChar()}
                buttongraph.setText("DETENER") }
            else{
                UDPComm().sendUDP("f${(13).toChar()}")          //${13.toChar()}
                graficando=false
                recv_on=false
                Thread.sleep(100)
                UDPComm().sendUDP("f${(13).toChar()}")
                Thread.sleep(100)
                socket.close()
                buttongraph.setText("GRAFICAR") }
        }
    }

    private fun visible_on(activ:Boolean) {
        if (activ){
            seekBarplotFrecCard.visibility=View.VISIBLE
            seekBarplotFrecResp.visibility=View.VISIBLE
            textViewFResp.visibility=View.VISIBLE
            textViewFCard.visibility=View.VISIBLE
            textViewFrecCard.visibility=View.VISIBLE
            textViewFrecResp.visibility=View.VISIBLE
        }
        else{
            seekBarplotFrecCard.visibility=View.INVISIBLE
            seekBarplotFrecResp.visibility=View.INVISIBLE
            textViewFResp.visibility=View.INVISIBLE
            textViewFCard.visibility=View.INVISIBLE
            textViewFrecCard.visibility=View.INVISIBLE
            textViewFrecResp.visibility=View.INVISIBLE
        }
    }

    private fun init_graph() {

        Grafica1=linechartMonitor
        Grafica1.description.isEnabled=true
        Grafica1.description.text="Captura en tiempo Real"
        Grafica1.setVisibleXRangeMaximum(Tmax.toFloat())
        Grafica1.setTouchEnabled(true)
        Grafica1.setScaleEnabled(true)
        Grafica1.setDrawGridBackground(false)
        Grafica1.setPinchZoom(false)
        Grafica1.setBorderColor(Color.BLACK)
        Grafica1.setBackgroundColor(Color.TRANSPARENT)

        //Grafica1.setVisibleXRange(0f, Tmax)
        var EjeY_R: YAxis =Grafica1.axisRight
        EjeY_R.setDrawLabels(false)
        var EjeY: YAxis =Grafica1.axisLeft
        EjeY.axisMaximum=1300f
        EjeY.axisMinimum=-40f
        EjeY.setDrawLabels(false)

        var Ejex: XAxis =Grafica1.xAxis
        Ejex.position= XAxis.XAxisPosition.BOTH_SIDED
        Ejex.axisMinimum=0f
        Ejex.axisMaximum=36000f
        Ejex.granularity=0.5f

            DatoECG= Entry(0.toFloat(),1060.toFloat())
        DatoPulse= Entry(0.toFloat(),850.toFloat())
        DatoVolResp= Entry(0.toFloat(),640.toFloat())
        DatoPress= Entry(0.toFloat(),430.toFloat())
        DatoSatO= Entry(0.toFloat(),220.toFloat())
        DatoPCO2= Entry(0.toFloat(),10.toFloat())
        //DatoIntRR= Entry(0.toFloat(),10.toFloat())


        SetECG= LineDataSet(null,"ECG")
        SetECG.axisDependency=(YAxis.AxisDependency.LEFT)
        SetECG.lineWidth=2f
        SetECG.color=Color.RED
        SetECG.mode= LineDataSet.Mode.LINEAR
        SetECG.setDrawCircles(false)

        SetPulse= LineDataSet(null,"Pulso")
        SetPulse.axisDependency=(YAxis.AxisDependency.LEFT)
        SetPulse.lineWidth=2f
        SetPulse.color=Color.CYAN
        SetPulse.mode= LineDataSet.Mode.LINEAR
        SetPulse.setDrawCircles(false)


        SetVolResp= LineDataSet(null,"Vol_Resp")
        SetVolResp.axisDependency=(YAxis.AxisDependency.LEFT)
        SetVolResp.lineWidth=2f
        SetVolResp.color=Color.BLUE
        SetVolResp.mode= LineDataSet.Mode.LINEAR
        SetVolResp.setDrawCircles(false)

        SetPresion= LineDataSet(null,"Presión")
        SetPresion.axisDependency=(YAxis.AxisDependency.LEFT)
        SetPresion.lineWidth=2f
        SetPresion.color=Color.GREEN
        SetPresion.mode= LineDataSet.Mode.LINEAR
        SetPresion.setDrawCircles(false)

        SetSatO= LineDataSet(null,"SatO")
        SetSatO.axisDependency=(YAxis.AxisDependency.LEFT)
        SetSatO.lineWidth=2f
        SetSatO.color=Color.MAGENTA
        SetSatO.mode= LineDataSet.Mode.LINEAR
        SetSatO.setDrawCircles(false)

        SetPCO2=LineDataSet(null,"PCO2")
        SetPCO2.axisDependency=(YAxis.AxisDependency.LEFT)
        SetPCO2.lineWidth=2f
        SetPCO2.color=Color.GRAY
        SetPCO2.mode= LineDataSet.Mode.CUBIC_BEZIER
        SetPCO2.setDrawCircles(false)

        /*SetIntRR= LineDataSet(null,"IntRR")
        SetIntRR.axisDependency=(YAxis.AxisDependency.LEFT)
        SetIntRR.lineWidth=2f
        SetIntRR.color=Color.BLACK
        SetIntRR.mode= LineDataSet.Mode.LINEAR
        SetIntRR.setDrawCircles(false)*/

        Data_line.addDataSet(SetECG)
        Data_line.addDataSet(SetPulse)
        Data_line.addDataSet(SetVolResp)
        Data_line.addDataSet(SetPresion)
        Data_line.addDataSet(SetSatO)
        Data_line.addDataSet(SetPCO2)
        //Data_line.addDataSet(SetIntRR)


        Data_line.setDrawValues(false)      //Unable show numeric labels
        Grafica1.setVisibleXRangeMaximum(Tmax.toFloat())
        Data_line.addEntry(DatoECG,0)
        Data_line.addEntry(DatoPulse,1)
        Data_line.addEntry(DatoVolResp,2)
        Data_line.addEntry(DatoPress,3)
        Data_line.addEntry(DatoSatO,4)
        Data_line.addEntry(DatoPCO2,5)
        //Data_line.addEntry(DatoIntRR,6)
        Grafica1.data=Data_line

        TextViewTmax.setText(Tmax.toString())
        seekBartgraf.progress= Tmax
        seekBarplotFrecCard.progress= (FreqCard*10).toInt()
        textViewFCard.setText(FreqCard.toString())
        seekBarplotFrecResp.progress= (FreqResp*10).toInt()
        textViewFResp.setText(FreqResp.toString())

    }

    fun graficar(){
        if(lectura){
            for (k in 1 ..62){
                indexgraf= indexgraf+k
                DatoECG = Entry(time, (ecg_buff[k].toUByte().toInt()+1060).toFloat())
                DatoPulse= Entry(time, (pulse_buff[k].toUByte().toInt()+850).toFloat())
                DatoVolResp= Entry(time, (volResp_buff[k].toUByte().toInt()+640).toFloat())
                DatoPress= Entry(time, (press_buff[k].toUByte().toInt()+430).toFloat())
                DatoSatO= Entry(time, (satO_buff[k].toUByte().toInt()+220).toFloat())
                DatoPCO2= Entry(time, (CO2_buff[k].toUByte().toInt()+10).toFloat())
                //DatoIntRR= Entry(time, (intRR_buff[k].toUByte().toInt()+150).toFloat())
                Data_line.addEntry(DatoECG, 0)
                Data_line.addEntry(DatoPulse, 1)
                Data_line.addEntry(DatoVolResp, 2)
                Data_line.addEntry(DatoPress, 3)
                Data_line.addEntry(DatoSatO, 4)
                Data_line.addEntry(DatoPCO2, 5)
                //Data_line.addEntry(DatoIntRR, 6)
                time+=0.0078F }
            Data_line.notifyDataChanged()
            if (time >= Tmax) { Grafica1.moveViewToX(time - Tmax) }
            else { Grafica1.moveViewToX(0F) }
        }
        if (allVar_list.isNotEmpty()){
            sizeLista=allVar_list.size
            if (sizeLista>8){
                DispFrCardV.setText(allVar_list[1])
                DispFrVasc.setText(allVar_list[2])
                DispFrResp.setText(allVar_list[3])
                DispPresMed.setText(allVar_list[4])
                DispPresSist.setText(allVar_list[5])
                DispPresDiast.setText(allVar_list[6])
                DispSatO.setText(allVar_list[7])
                DispPCO2.setText(allVar_list[8])
            }

        }
    }

}