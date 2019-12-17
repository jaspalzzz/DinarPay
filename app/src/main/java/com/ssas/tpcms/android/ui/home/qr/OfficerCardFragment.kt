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
import com.ssas.tpcms.android.databinding.FragmentOfficerCardBinding
import com.ssas.tpcms.android.repo.home.HomeVM
import com.ssas.tpcms.android.ui.home.profile.UserProfileFragment

/**
 * Created by Jaspal on 12/12/2019.
 */

private const val ARG_PROFILE_DATA = "OFFICER_CODE"

class OfficerCardFragment : BaseBottomSheetFragment<FragmentOfficerCardBinding, HomeVM>() {
    var data: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        arguments?.let {
            data = it.getString(ARG_PROFILE_DATA).toString()
        }
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_officer_card, HomeVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel.getQRProfileReponse("45dfg3dfdg234dfg", "LYeGOV02897TP")
        if (!data.isEmpty()) {
            var profileData = Gson().fromJson<OfficersProfileResponseModel>(
                data,
                OfficersProfileResponseModel::class.java
            )
            binding.item = profileData
        }
        setListners()
    }


    private fun setListners() {
        binding.cancelBt.setOnClickListener {
            dismiss()
        }
    }

    override fun subscribeToEvents(vm: HomeVM) {

    }

    companion object {
        @JvmStatic
        fun newInstance(data: String) =
            OfficerCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROFILE_DATA, data)
                }
            }
    }
}