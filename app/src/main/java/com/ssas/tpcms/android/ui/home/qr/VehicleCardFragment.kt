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
import com.ssas.tpcms.android.data.models.qr.QrVehicleResponse
import com.ssas.tpcms.android.databinding.FragmentVehicleCardBinding
import com.ssas.tpcms.android.repo.home.HomeVM

/**
 * Created by Jaspal on 12/12/2019.
 */

const val ARG_VEHICLE_DATA = "ARG_VEHICLE_DATA"

class VehicleCardFragment : BaseBottomSheetFragment<FragmentVehicleCardBinding, HomeVM>() {

    var data: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        arguments?.let {
            data = it.getString(ARG_VEHICLE_DATA).toString()
        }
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_vehicle_card, HomeVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel.getQRProfileReponse("45dfg3dfdg234dfg", "LYeGOV02897TP")
        if (!data.isEmpty()) {
            var vehicleData = Gson().fromJson<QrVehicleResponse>(
                data,
                QrVehicleResponse::class.java
            )
            binding.item = vehicleData
        }
        setListners()
    }

    private fun setListners() {
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun subscribeToEvents(vm: HomeVM) {

    }

    companion object {
        @JvmStatic
        fun newInstance(data: String) =
            VehicleCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VEHICLE_DATA, data)
                }
            }
    }


}