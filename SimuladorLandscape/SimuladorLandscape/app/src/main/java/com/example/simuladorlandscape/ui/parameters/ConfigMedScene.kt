package com.example.simuladorlandscape.ui.parameters


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simuladorlandscape.*

import kotlinx.android.synthetic.main.fragment_config_med_scene.*

/**
 * A simple [Fragment] subclass.
 */
class ConfigMedScene : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config_med_scene, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargardatosScene()

        buttonSaveScene.setOnClickListener {

            FreqCard_Scen[PosScene]= editTextScFc.text.toString().toFloat()
            FreqResp_Scen[PosScene]=editTextScFr.text.toString().toFloat()
            Peso_Scen[PosScene]=editTextScW.text.toString().toFloat()
            Temp_Scen[PosScene]=editTextScT.text.toString().toFloat()
            PH_Scen[PosScene]=editTextScPH.text.toString().toFloat()
            DesvFc_Scen[PosScene]=editTextScDFc.text.toString().toFloat()
            LF_HF_Scen[PosScene]=editTextScLF_HF.text.toString().toFloat()
            F_lf_Scen[PosScene]=editTextScLF.text.toString().toFloat()
            F_hf_Scen[PosScene]=editTextScHF.text.toString().toFloat()
            Desv_lf_Scen[PosScene]=editTextScDLF.text.toString().toFloat()
            Desv_hf_Scen[PosScene]=editTextScDHF.text.toString().toFloat()

            ThetP_Scen[PosScene]=editTextScThP.text.toString().toFloat()
            ThetQ_Scen[PosScene]=editTextScThQ.text.toString().toFloat()
            ThetR_Scen[PosScene]=editTextScThR.text.toString().toFloat()
            ThetS_Scen[PosScene]=editTextScThS.text.toString().toFloat()
            ThetT_Scen[PosScene]=editTextScThT.text.toString().toFloat()

            aP_Scen[PosScene]=editTextScaP.text.toString().toFloat()
            aQ_Scen[PosScene]=editTextScaQ.text.toString().toFloat()
            aR_Scen[PosScene]=editTextScaR.text.toString().toFloat()
            aS_Scen[PosScene]=editTextScaS.text.toString().toFloat()
            aT_Scen[PosScene]=editTextScaT.text.toString().toFloat()

            bP_Scen[PosScene]=editTextScbP.text.toString().toFloat()
            bQ_Scen[PosScene]=editTextScbQ.text.toString().toFloat()
            bR_Scen[PosScene]=editTextScbR.text.toString().toFloat()
            bS_Scen[PosScene]=editTextScbS.text.toString().toFloat()
            bT_Scen[PosScene]=editTextScbT.text.toString().toFloat()

            ThetOI_Scen[PosScene]=editTextScThOI.text.toString().toFloat()
            ThetOR_Scen[PosScene]=editTextScThOR.text.toString().toFloat()

            aOI_Scen[PosScene]=editTextScaOI.text.toString().toFloat()
            aOR_Scen[PosScene]=editTextScaOR.text.toString().toFloat()

            bOI_Scen[PosScene]=editTextScbOI.text.toString().toFloat()
            bOR_Scen[PosScene]=editTextScbOR.text.toString().toFloat()

            DPmax_Scen[PosScene]=editTextScDPmax.text.toString().toFloat()
            Patm_Scen[PosScene]=editTextScPatm.text.toString().toFloat()
            Tinsp_Scen[PosScene]=editTextScTinsp.text.toString().toFloat()
            Rresp_Scen[PosScene]=editTextScRresp.text.toString().toFloat()
            CapP_Scen[PosScene]=editTextScbCPulm.text.toString().toFloat()
            VD_Scen[PosScene]=editTextScVD.text.toString().toFloat()

            cHb_Scen[PosScene]=editTextSccHb.text.toString().toFloat()
            Sh_Scen[PosScene]=editTextScShunt.text.toString().toFloat()
            cTO2_Scen[PosScene]=editTextSccTO2.text.toString().toFloat()
            indR_Scen[PosScene]=editTextScIndResp.text.toString().toFloat()

            Eaimax_Scen[PosScene]=editTextScEaimax.text.toString().toFloat()
            Eaimin_Scen[PosScene]=editTextScEaimin.text.toString().toFloat()
            Evimax_Scen[PosScene]=editTextScEvimax.text.toString().toFloat()
            Evimin_Scen[PosScene]=editTextScEvimin.text.toString().toFloat()
            Eait_Scen[PosScene]=editTextScEait.text.toString().toFloat()
            Eaet_Scen[PosScene]=editTextScEaet.text.toString().toFloat()
            Evet_Scen[PosScene]=editTextScEvet.text.toString().toFloat()
            Evit_Scen[PosScene]=editTextScEvit.text.toString().toFloat()
            Eadmax_Scen[PosScene]=editTextScEadmax.text.toString().toFloat()
            Eadmin_Scen[PosScene]=editTextScEadmin.text.toString().toFloat()
            Evdmax_Scen[PosScene]=editTextScEvdmax.text.toString().toFloat()
            Evdmin_Scen[PosScene]=editTextScEvdmin.text.toString().toFloat()
            Eap_Scen[PosScene]=editTextScEap.text.toString().toFloat()
            Evp_Scen[PosScene]=editTextScEvp.text.toString().toFloat()

            ConsaitO2_Scen[PosScene]=editTextScMaitO2.text.toString().toFloat()
            ConsaetO2_Scen[PosScene]=editTextScMaetO2.text.toString().toFloat()
            ConsvpO2_Scen[PosScene]=editTextScMvpO2.text.toString().toFloat()

            Rai_Scen[PosScene]=editTextScRai.text.toString().toFloat()
            Rvi_Scen[PosScene]=editTextScRvi.text.toString().toFloat()

            Rait_Scen[PosScene]=editTextScRai.text.toString().toFloat()
            Raet_Scen[PosScene]=editTextScRaet.text.toString().toFloat()
            Rvet_Scen[PosScene]=editTextScRvet.text.toString().toFloat()
            Rvit_Scen[PosScene]=editTextScRvit.text.toString().toFloat()
            Rad_Scen[PosScene]=editTextScRad.text.toString().toFloat()
            Rvd_Scen[PosScene]=editTextScRvd.text.toString().toFloat()
            Rap_Scen[PosScene]=editTextScRap.text.toString().toFloat()
            Rvp_Scen[PosScene]=editTextScRvp.text.toString().toFloat()

        }
    }

    private fun cargardatosScene() {
        editTextScFc.setText(FreqCard_Scen[PosScene].toString())
        editTextScFr.setText(FreqResp_Scen[PosScene].toString())
        editTextScW.setText(Peso_Scen[PosScene].toString())
        editTextScT.setText(Temp_Scen[PosScene].toString())
        editTextScPH.setText(PH_Scen[PosScene].toString())
        editTextScDFc.setText(DesvFc_Scen[PosScene].toString())
        editTextScLF_HF.setText(LF_HF_Scen[PosScene].toString())
        editTextScLF.setText(F_lf_Scen[PosScene].toString())
        editTextScHF.setText(F_hf_Scen[PosScene].toString())
        editTextScDLF.setText(Desv_lf_Scen[PosScene].toString())
        editTextScDHF.setText(Desv_hf_Scen[PosScene].toString())

        editTextScThP.setText(ThetP_Scen[PosScene].toString())
        editTextScThQ.setText(ThetQ_Scen[PosScene].toString())
        editTextScThR.setText(ThetR_Scen[PosScene].toString())
        editTextScThS.setText(ThetS_Scen[PosScene].toString())
        editTextScThT.setText(ThetT_Scen[PosScene].toString())

        editTextScaP.setText(aP_Scen[PosScene].toString())
        editTextScaQ.setText(aQ_Scen[PosScene].toString())
        editTextScaR.setText(aR_Scen[PosScene].toString())
        editTextScaS.setText(aS_Scen[PosScene].toString())
        editTextScaT.setText(aT_Scen[PosScene].toString())

        editTextScbP.setText(bP_Scen[PosScene].toString())
        editTextScbQ.setText(bQ_Scen[PosScene].toString())
        editTextScbR.setText(bR_Scen[PosScene].toString())
        editTextScbS.setText(bS_Scen[PosScene].toString())
        editTextScbT.setText(bT_Scen[PosScene].toString())

        editTextScThOI.setText(ThetOI_Scen[PosScene].toString())
        editTextScThOR.setText(ThetOR_Scen[PosScene].toString())

        editTextScaOI.setText(aOI_Scen[PosScene].toString())
        editTextScaOR.setText(aOR_Scen[PosScene].toString())

        editTextScbOI.setText(bOI_Scen[PosScene].toString())
        editTextScbOR.setText(bOR_Scen[PosScene].toString())

        editTextScDPmax.setText(DPmax_Scen[PosScene].toString())
        editTextScPatm.setText(Patm_Scen[PosScene].toString())
        editTextScTinsp.setText(Tinsp_Scen[PosScene].toString())
        editTextScRresp.setText(Rresp_Scen[PosScene].toString())
        editTextScbCPulm.setText(CapP_Scen[PosScene].toString())
        editTextScVD.setText(VD_Scen[PosScene].toString())

        editTextSccHb.setText(cHb_Scen[PosScene].toString())
        editTextScShunt.setText(Sh_Scen[PosScene].toString())
        editTextSccTO2.setText(cTO2_Scen[PosScene].toString())
        editTextScIndResp.setText(indR_Scen[PosScene].toString())

        editTextScEaimax.setText(Eaimax_Scen[PosScene].toString())
        editTextScEaimin.setText(Eaimin_Scen[PosScene].toString())
        editTextScEvimax.setText(Evimax_Scen[PosScene].toString())
        editTextScEvimin.setText(Evimin_Scen[PosScene].toString())
        editTextScEait.setText(Eait_Scen[PosScene].toString())
        editTextScEaet.setText(Eaet_Scen[PosScene].toString())
        editTextScEvet.setText(Evet_Scen[PosScene].toString())
        editTextScEvit.setText(Evit_Scen[PosScene].toString())
        editTextScEadmax.setText(Eadmax_Scen[PosScene].toString())
        editTextScEadmin.setText(Eadmin_Scen[PosScene].toString())
        editTextScEvdmax.setText(Evdmax_Scen[PosScene].toString())
        editTextScEvdmin.setText(Evdmin_Scen[PosScene].toString())
        editTextScEap.setText(Eap_Scen[PosScene].toString())
        editTextScEvp.setText(Evp_Scen[PosScene].toString())

        editTextScMaitO2.setText(ConsaitO2_Scen[PosScene].toString())
        editTextScMaetO2.setText(ConsaetO2_Scen[PosScene].toString())
        editTextScMvpO2.setText(ConsvpO2_Scen[PosScene].toString())

        editTextScRai.setText(Rai_Scen[PosScene].toString())
        editTextScRvi.setText(Rvi_Scen[PosScene].toString())
        editTextScRait.setText(Rait_Scen[PosScene].toString())
        editTextScRaet.setText(Raet_Scen[PosScene].toString())
        editTextScRvet.setText(Rvet_Scen[PosScene].toString())
        editTextScRvit.setText(Rvit_Scen[PosScene].toString())
        editTextScRad.setText(Rad_Scen[PosScene].toString())
        editTextScRvd.setText(Rvd_Scen[PosScene].toString())
        editTextScRap.setText(Rap_Scen[PosScene].toString())
        editTextScRvp.setText(Rvp_Scen[PosScene].toString())
    }


}
