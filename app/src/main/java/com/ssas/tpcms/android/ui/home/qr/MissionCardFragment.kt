package com.ssas.tpcms.android.ui.home.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.data.models.qr.MissionOfficerResponse
import com.ssas.tpcms.android.data.models.qr.QrVehicleResponse
import com.ssas.tpcms.android.databinding.FragmentMissionCardBinding
import com.ssas.tpcms.android.repo.home.HomeVM

/**
 * Created by Jaspal on 12/12/2019.
 */

const val ARG_MISSION_DATA = "ARG_MISSION_DATA"
const val ARG_MISSION_PROFILE_DATA = "ARG_MISSION_PROFILE_DATA"

class MissionCardFragment : BaseBottomSheetFragment<FragmentMissionCardBinding, HomeVM>() {

    var dataMission: String = ""
    var dataProfile: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        arguments?.let {
            dataMission = it.getString(ARG_MISSION_DATA).toString()
            dataProfile = it.getString(ARG_MISSION_PROFILE_DATA).toString()
        }
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_mission_card, HomeVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {
    }

    override fun subscribeToEvents(vm: HomeVM) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!dataMission.isEmpty()) {
            var missionData = Gson().fromJson<MissionOfficerResponse>(
                dataMission, MissionOfficerResponse::class.java
            )
            binding.missionItem = missionData
        }

        if (!dataProfile.isEmpty()) {
            var profileData = Gson().fromJson<OfficersProfileResponseModel>(
                dataProfile,
                OfficersProfileResponseModel::class.java
            )
            binding.profileItem = profileData
        }
        setListeners()
    }

    fun setListeners() {
        binding.cancelBt.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(dataMission: String, dataProfile: String) =
            MissionCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MISSION_DATA, dataMission)
                    putString(ARG_MISSION_PROFILE_DATA, dataProfile)
                }
            }
    }

}