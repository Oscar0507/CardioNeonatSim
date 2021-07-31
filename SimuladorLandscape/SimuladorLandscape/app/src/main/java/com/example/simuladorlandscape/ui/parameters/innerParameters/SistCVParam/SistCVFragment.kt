package com.example.simuladorlandscape.ui.parameters.innerParameters.SistCVParam


import android.os.Bundle
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
import com.example.simuladorlandscape.ui.parameters.innerParameters.MecRespParam.MecRespViewModel
import kotlinx.android.synthetic.main.fragment_mec_resp.*
import kotlinx.android.synthetic.main.fragment_params.*
import kotlinx.android.synthetic.main.fragment_params.editTextPH
import kotlinx.android.synthetic.main.fragment_plots.*
import kotlinx.android.synthetic.main.fragment_sist_cv.*
import java.net.DatagramSocket

/**
 * A simple [Fragment] subclass.
 */
class SistCVFragment : Fragment() {

    var maxcHb:Float=0.24F
    var mincHb:Float=0.14F
    var maxConsTot:Float=0.18F
    var minConsTot:Float=0.11F
    var maxShunt:Float=0.5F
    var minShunt:Float=0F
    var maxIndResp:Float=1.2F
    var minIndResp:Float=0.6F

    var maxElast:Float=100F
    var minElast:Float=0.1F

    var maxConO:Float=1F
    var minConO:Float=0F

    var maxRes:Float=10F
    var minRes:Float=0.01F

    private lateinit var sistCVViewModelView: SistCVViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sistCVViewModelView =
            ViewModelProviders.of(this).get(SistCVViewModel::class.java)
        return inflater.inflate(R.layout.fragment_sist_cv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wifiObserver= Observer<Int> { if(it==2) { mostrarBotonesCV(true)
                                        avisoNoconex.visibility=View.INVISIBLE }
                                        else { mostrarBotonesCV(false)
                                        avisoNoconex.visibility=View.VISIBLE } }
        sistCVViewModelView.wifiState.observe(viewLifecycleOwner,wifiObserver)
        carga_datosCV_defecto()


        //Eventos de botones

        buttonEnvTodSCV.setOnClickListener {

            cHbSang=editTexHb.text.toString().toFloat()
            Shunt=editTextShunt.text.toString().toFloat()
            ConsTotO2=editTextConsTotO2.text.toString().toFloat()
            IndResp=editTextIndResp.text.toString().toFloat()

            Eaimax=editTextEaimax.text.toString().toFloat()
            Eaimin=editTextEaimin.text.toString().toFloat()
            Evimax=editTextEvimax.text.toString().toFloat()
            Evimin=editTextEvimin.text.toString().toFloat()
            Eait=editTextEait.text.toString().toFloat()
            Eaet=editTextEaet.text.toString().toFloat()
            Evet=editTextEvet.text.toString().toFloat()
            Evit=editTextEvit.text.toString().toFloat()

            Eadmax=editTextEadmax.text.toString().toFloat()
            Eaimin=editTextEadmin.text.toString().toFloat()
            Evdmax=editTextEvdmax.text.toString().toFloat()
            Evdmin=editTextEvdmin.text.toString().toFloat()

            Eap=editTextEap.text.toString().toFloat()
            Evp=editTextEvp.text.toString().toFloat()

            ConsvpO2=editTextMvpO2.text.toString().toFloat()
            ConsaitO2=editTextMaitO2.text.toString().toFloat()
            ConsaetO2=editTextMaetO2.text.toString().toFloat()

            Rai=editTextRai.text.toString().toFloat()
            Rvi=editTextRvi.text.toString().toFloat()
            Rait=editTextRait.text.toString().toFloat()
            Raet=editTextRaet.text.toString().toFloat()
            Rvet=editTextRvet.text.toString().toFloat()
            Rvit=editTextRvit.text.toString().toFloat()
            R_ad=editTextRad.text.toString().toFloat()
            Rvd=editTextRvd.text.toString().toFloat()
            Rap=editTextRap.text.toString().toFloat()
            Rvp=editTextRvp.text.toString().toFloat()

            if (socket.isClosed){ socket= DatagramSocket(simPort) }

            UDPComm().sendUDP("n${cHbSang.toString()}@")
            UDPComm().sendUDP(">${Shunt.toString()}@")
            UDPComm().sendUDP("o${ConsTotO2.toString()}@")
            UDPComm().sendUDP("p${IndResp.toString()}@")

            UDPComm().sendUDP("1${Eaimax.toString()}@")
            UDPComm().sendUDP("2${Eaimin.toString()}@")
            UDPComm().sendUDP("3${Evimax.toString()}@")
            UDPComm().sendUDP("4${Evimin.toString()}@")
            UDPComm().sendUDP("5${Eait.toString()}@")
            UDPComm().sendUDP("6${Eaet.toString()}@")
            UDPComm().sendUDP("7${Evet.toString()}@")
            UDPComm().sendUDP("8${Evit.toString()}@")
            UDPComm().sendUDP("9${Eadmax.toString()}@")
            UDPComm().sendUDP("0${Eadmin.toString()}@")
            UDPComm().sendUDP("!${Evdmax.toString()}@")
            UDPComm().sendUDP("#${Evdmin.toString()}@")
            UDPComm().sendUDP("$${Eap.toString()}@")
            UDPComm().sendUDP("%${Evp.toString()}@")

            UDPComm().sendUDP("*${ConsvpO2.toString()}@")
            UDPComm().sendUDP("(${ConsaitO2.toString()}@")
            UDPComm().sendUDP(")${ConsaetO2.toString()}@")

            UDPComm().sendUDP("&${Rai.toString()}@")
            UDPComm().sendUDP(",${Rvi.toString()}@")
            UDPComm().sendUDP("+${Rait.toString()}@")
            UDPComm().sendUDP("-${Raet.toString()}@")
            UDPComm().sendUDP(".${Rvet.toString()}@")
            UDPComm().sendUDP("/${Rvit.toString()}@")
            UDPComm().sendUDP("[${R_ad.toString()}@")
            UDPComm().sendUDP("]${Rvd.toString()}@")
            UDPComm().sendUDP(":${Rap.toString()}@")
            UDPComm().sendUDP(";${Rvp.toString()}@")

            if (!graficando){ socket.close() }
        }

        buttonHb.setOnClickListener {
            cHbSang=editTexHb.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("n${cHbSang.toString()}@")
            if (!graficando){ socket.close() }
        }
        buttonShunt.setOnClickListener {
            Shunt=editTextShunt.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP(">${Shunt.toString()}@")
            if (!graficando){ socket.close() }
        }
        buttonConsTotO2.setOnClickListener {
            ConsTotO2=editTextConsTotO2.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("o${ConsTotO2.toString()}@")
            if (!graficando){ socket.close() }
        }
        buttonIndResp.setOnClickListener {
            IndResp=editTextIndResp.text.toString().toFloat()
            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("p${IndResp.toString()}@")
            if (!graficando){ socket.close() }
        }
        buttonEnvParamCV.setOnClickListener {

            Eaimax=editTextEaimax.text.toString().toFloat()
            Eaimin=editTextEaimin.text.toString().toFloat()
            Evimax=editTextEvimax.text.toString().toFloat()
            Evimin=editTextEvimin.text.toString().toFloat()
            Eait=editTextEait.text.toString().toFloat()
            Eaet=editTextEaet.text.toString().toFloat()
            Evet=editTextEvet.text.toString().toFloat()
            Evit=editTextEvit.text.toString().toFloat()

            Eadmax=editTextEadmax.text.toString().toFloat()
            Eaimin=editTextEadmin.text.toString().toFloat()
            Evdmax=editTextEvdmax.text.toString().toFloat()
            Evdmin=editTextEvdmin.text.toString().toFloat()

            Eap=editTextEap.text.toString().toFloat()
            Evp=editTextEvp.text.toString().toFloat()

            ConsvpO2=editTextMvpO2.text.toString().toFloat()
            ConsaitO2=editTextMaitO2.text.toString().toFloat()
            ConsaetO2=editTextMaetO2.text.toString().toFloat()

            Rai=editTextRai.text.toString().toFloat()
            Rvi=editTextRvi.text.toString().toFloat()
            Rait=editTextRait.text.toString().toFloat()
            Raet=editTextRaet.text.toString().toFloat()
            Rvet=editTextRvet.text.toString().toFloat()
            Rvit=editTextRvit.text.toString().toFloat()
            R_ad=editTextRad.text.toString().toFloat()
            Rvd=editTextRvd.text.toString().toFloat()
            Rap=editTextRap.text.toString().toFloat()
            Rvp=editTextRvp.text.toString().toFloat()

            if (socket.isClosed){ socket= DatagramSocket(simPort) }
            UDPComm().sendUDP("1${Eaimax.toString()}@")
            UDPComm().sendUDP("2${Eaimin.toString()}@")
            UDPComm().sendUDP("3${Evimax.toString()}@")
            UDPComm().sendUDP("4${Evimin.toString()}@")
            UDPComm().sendUDP("5${Eait.toString()}@")
            UDPComm().sendUDP("6${Eaet.toString()}@")
            UDPComm().sendUDP("7${Evet.toString()}@")
            UDPComm().sendUDP("8${Evit.toString()}@")
            UDPComm().sendUDP("9${Eadmax.toString()}@")
            UDPComm().sendUDP("0${Eadmin.toString()}@")
            UDPComm().sendUDP("!${Evdmax.toString()}@")
            UDPComm().sendUDP("#${Evdmin.toString()}@")
            UDPComm().sendUDP("$${Eap.toString()}@")
            UDPComm().sendUDP("%${Evp.toString()}@")

            UDPComm().sendUDP("*${ConsvpO2.toString()}@")
            UDPComm().sendUDP("(${ConsaitO2.toString()}@")
            UDPComm().sendUDP(")${ConsaetO2.toString()}@")

            UDPComm().sendUDP("&${Rai.toString()}@")
            UDPComm().sendUDP(",${Rvi.toString()}@")
            UDPComm().sendUDP("+${Rait.toString()}@")
            UDPComm().sendUDP("-${Raet.toString()}@")
            UDPComm().sendUDP(".${Rvet.toString()}@")
            UDPComm().sendUDP("/${Rvit.toString()}@")
            UDPComm().sendUDP("[${R_ad.toString()}@")
            UDPComm().sendUDP("]${Rvd.toString()}@")
            UDPComm().sendUDP(":${Rap.toString()}@")
            UDPComm().sendUDP(";${Rvp.toString()}@")
            if (!graficando){ socket.close() }
        }

        editTexHb.setOnEditorActionListener(captureDataEdtTxt(editTexHb,maxcHb,mincHb))
        editTextConO2.setOnEditorActionListener(captureDataEdtTxt(editTextConsTotO2,maxConsTot,minConsTot))
        editTextShunt.setOnEditorActionListener(captureDataEdtTxt(editTextShunt,maxShunt,minShunt))
        editTextIndResp.setOnEditorActionListener(captureDataEdtTxt(editTextIndResp,maxIndResp,minIndResp))

        editTextEaimax.setOnEditorActionListener(captureDataEdtTxt(editTextEaimax,maxElast,minElast))
        editTextEaimin.setOnEditorActionListener(captureDataEdtTxt(editTextEaimin,maxElast,minElast))
        editTextEvimax.setOnEditorActionListener(captureDataEdtTxt(editTextEvimax,maxElast,minElast))
        editTextEvimin.setOnEditorActionListener(captureDataEdtTxt(editTextEvimin,maxElast,minElast))
        editTextEait.setOnEditorActionListener(captureDataEdtTxt(editTextEait,maxElast,minElast))
        editTextEaet.setOnEditorActionListener(captureDataEdtTxt(editTextEaet,maxElast,minElast))
        editTextEvit.setOnEditorActionListener(captureDataEdtTxt(editTextEvit,maxElast,minElast))
        editTextEadmax.setOnEditorActionListener(captureDataEdtTxt(editTextEadmax,maxElast,minElast))
        editTextEadmin.setOnEditorActionListener(captureDataEdtTxt(editTextEadmin,maxElast,minElast))
        editTextEvdmax.setOnEditorActionListener(captureDataEdtTxt(editTextEvdmax,maxElast,minElast))
        editTextEvdmin.setOnEditorActionListener(captureDataEdtTxt(editTextEvdmin,maxElast,minElast))
        editTextEap.setOnEditorActionListener(captureDataEdtTxt(editTextEap,maxElast,minElast))
        editTextEvp.setOnEditorActionListener(captureDataEdtTxt(editTextEvp,maxElast,minElast))

        editTextMaitO2.setOnEditorActionListener(captureDataEdtTxt(editTextMaitO2,maxConO,minConO))
        editTextMaetO2.setOnEditorActionListener(captureDataEdtTxt(editTextMaetO2,maxConO,minConO))
        editTextMvpO2.setOnEditorActionListener(captureDataEdtTxt(editTextMvpO2,maxConO,minConO))

        editTextRai.setOnEditorActionListener(captureDataEdtTxt(editTextRai,maxRes,minRes))
        editTextRvi.setOnEditorActionListener(captureDataEdtTxt(editTextRvi,maxRes,minRes))
        editTextRait.setOnEditorActionListener(captureDataEdtTxt(editTextRait,maxRes,minRes))
        editTextRaet.setOnEditorActionListener(captureDataEdtTxt(editTextRaet,maxRes,minRes))
        editTextRvit.setOnEditorActionListener(captureDataEdtTxt(editTextRvit,maxRes,minRes))
        editTextRvet.setOnEditorActionListener(captureDataEdtTxt(editTextRvet,maxRes,minRes))
        editTextRad.setOnEditorActionListener(captureDataEdtTxt(editTextRad,maxRes,minRes))
        editTextRvd.setOnEditorActionListener(captureDataEdtTxt(editTextRvd,maxRes,minRes))
        editTextRap.setOnEditorActionListener(captureDataEdtTxt(editTextRap,maxRes,minRes))
        editTextRvp.setOnEditorActionListener(captureDataEdtTxt(editTextRvp,maxRes,minRes))
    }



    private fun mostrarBotonesCV(en: Boolean) {
        if(en){
            buttonHb.visibility=View.VISIBLE
            buttonShunt.visibility=View.VISIBLE
            buttonConsTotO2.visibility=View.VISIBLE
            buttonIndResp.visibility=View.VISIBLE
            buttonEnvParamCV.visibility=View.VISIBLE
            buttonEnvTodSCV.visibility=View.VISIBLE
        }
        else{
            buttonHb.visibility=View.INVISIBLE
            buttonShunt.visibility=View.INVISIBLE
            buttonConsTotO2.visibility=View.INVISIBLE
            buttonIndResp.visibility=View.INVISIBLE
            buttonEnvParamCV.visibility=View.INVISIBLE
            buttonEnvTodSCV.visibility=View.INVISIBLE
        }
    }

    fun captureDataEdtTxt(edText: EditText, max:Float, min:Float)=object: TextView.OnEditorActionListener{
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            var valcapt: String = edText.text.toString()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (valcapt.toFloat() >= min && valcapt.toFloat() <= max) {
                    when (edText) {
                        editTextFreqCard -> {
                            FreqCard = valcapt.toFloat()
                        }
                        editTexHb -> {
                            cHbSang = valcapt.toFloat()
                        }
                        editTextConsTotO2 -> {
                            ConsTotO2 = valcapt.toFloat()
                        }
                        editTextShunt -> {
                            Shunt = valcapt.toFloat()
                        }
                        editTextIndResp -> {
                            IndResp = valcapt.toFloat()
                        }

                        editTextEaimax -> {
                            Eaimax = valcapt.toFloat()
                        }
                        editTextEaimin -> {
                            Eaimin = valcapt.toFloat()
                        }
                        editTextEvimax -> {
                            Evimax = valcapt.toFloat()
                        }
                        editTextEvimin -> {
                            Evimin = valcapt.toFloat()
                        }
                        editTextEait -> {
                            Eait = valcapt.toFloat()
                        }
                        editTextEaet -> {
                            Eaet = valcapt.toFloat()
                        }
                        editTextEvet -> {
                            Evet = valcapt.toFloat()
                        }
                        editTextEvit -> {
                            Evit = valcapt.toFloat()
                        }
                        editTextEadmax -> {
                            Eadmax = valcapt.toFloat()
                        }
                        editTextEadmin -> {
                            Eadmin = valcapt.toFloat()
                        }
                        editTextEvdmax -> {
                            Evdmax = valcapt.toFloat()
                        }
                        editTextEvdmin -> {
                            Evdmin = valcapt.toFloat()
                        }
                        editTextEap -> {
                            Eap = valcapt.toFloat()
                        }
                        editTextEvp -> {
                            Evp = valcapt.toFloat()
                        }

                        editTextMaitO2 -> {
                            ConsaitO2 = valcapt.toFloat()
                        }
                        editTextMaetO2 -> {
                            ConsaetO2 = valcapt.toFloat()
                        }
                        editTextMvpO2 -> {
                            ConsvpO2 = valcapt.toFloat()
                        }

                        editTextRai -> {
                            Rai = valcapt.toFloat()
                        }
                        editTextRvi -> {
                            Rvi = valcapt.toFloat()
                        }
                        editTextRait -> {
                            Rait = valcapt.toFloat()
                        }
                        editTextRaet -> {
                            Raet = valcapt.toFloat()
                        }
                        editTextRvet -> {
                            Rvet = valcapt.toFloat()
                        }
                        editTextRvit -> {
                            Rvit = valcapt.toFloat()
                        }
                        editTextRad -> {
                            R_ad = valcapt.toFloat()
                        }
                        editTextRvd -> {
                            Rvd = valcapt.toFloat()
                        }
                        editTextRap -> {
                            Rap = valcapt.toFloat()
                        }
                        editTextRvp -> {
                            Rvp = valcapt.toFloat()
                        }
                    } }
            else {
                    Toast.makeText(context,"El valor debe estar entre ${min} y ${max}", Toast.LENGTH_SHORT).show()

                    when (edText) {
                        editTextFreqCard -> { edText.setText(FreqCard.toString()) }
                        editTextFreqCard -> { edText.setText(FreqCard.toString()) }
                        editTexHb -> {  edText.setText(cHbSang.toString()) }
                        editTextConsTotO2 -> { edText.setText(ConsTotO2.toString()) }
                        editTextShunt -> { edText.setText(Shunt.toString()) }
                        editTextIndResp -> { edText.setText(IndResp.toString()) }
                        editTextEaimax -> { edText.setText(Eaimax.toString()) }
                        editTextEaimin -> { edText.setText(Eaimin.toString()) }
                        editTextEvimax -> { edText.setText(Evimax.toString()) }
                        editTextEvimin -> { edText.setText(Evimin.toString()) }
                        editTextEait -> { edText.setText(Eait.toString()) }
                        editTextEaet -> { edText.setText(Eaet.toString()) }
                        editTextEvet -> { edText.setText(Evet.toString()) }
                        editTextEvit -> { edText.setText(Evit.toString()) }
                        editTextEadmax -> { edText.setText(Eadmax.toString()) }
                        editTextEadmin -> { edText.setText(Eadmin.toString()) }
                        editTextEvdmax -> { edText.setText(Evdmax.toString()) }
                        editTextEvdmin -> { edText.setText(Evdmin.toString()) }
                        editTextEap -> { edText.setText(Eap.toString()) }
                        editTextEvp -> { edText.setText(Evp.toString()) }
                        editTextMaitO2 -> { edText.setText(ConsaitO2.toString()) }
                        editTextMaetO2 -> { edText.setText(ConsaetO2.toString()) }
                        editTextMvpO2 -> { edText.setText(ConsvpO2.toString()) }
                        editTextRai -> { edText.setText(Rai.toString()) }
                        editTextRvi -> { edText.setText(Rvi.toString()) }
                        editTextRait -> { edText.setText(Rait.toString()) }
                        editTextRaet -> { edText.setText(Raet.toString()) }
                        editTextRvet -> { edText.setText(Rvet.toString()) }
                        editTextRvit -> { edText.setText(Rvit.toString()) }
                        editTextRad -> { edText.setText(R_ad.toString()) }
                        editTextRvd -> { edText.setText(Rvd.toString()) }
                        editTextRap -> { edText.setText(Rap.toString()) }
                        editTextRvp -> { edText.setText(Rvp.toString()) }
                    }
                } }
                return false
            }
        }

    private fun carga_datosCV_defecto() {
        editTexHb.setText(cHbSang.toString())
        editTextConsTotO2.setText(ConsTotO2.toString())
        editTextIndResp.setText(IndResp.toString())
        editTextShunt.setText(Shunt.toString())

        editTextEaimax.setText(Eaimax.toString())
        editTextEaimin.setText(Eaimin.toString())
        editTextEvimax.setText(Evimax.toString())
        editTextEvimin.setText(Evimin.toString())
        editTextEait.setText(Eait.toString())
        editTextEaet.setText(Eaet.toString())
        editTextEvet.setText(Evet.toString())
        editTextEvit.setText(Evit.toString())
        editTextEadmax.setText(Eadmax.toString())
        editTextEadmin.setText(Eadmin.toString())
        editTextEvdmax.setText(Evdmax.toString())
        editTextEvdmin.setText(Evdmin.toString())
        editTextEap.setText(Eap.toString())
        editTextEvp.setText(Evp.toString())

        editTextMaitO2.setText(ConsaitO2.toString())
        editTextMaetO2.setText(ConsaetO2.toString())
        editTextMvpO2.setText(ConsvpO2.toString())

        editTextRai.setText(Rai.toString())
        editTextRvi.setText(Rvi.toString())
        editTextRait.setText(Rait.toString())
        editTextRaet.setText(Raet.toString())
        editTextRvet.setText(Rvet.toString())
        editTextRvit.setText(Rvit.toString())
        editTextRad.setText(R_ad.toString())
        editTextRvd.setText(Rvd.toString())
        editTextRap.setText(Rap.toString())
        editTextRvp.setText(Rvp.toString())

    }

}
