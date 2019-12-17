package com.ssas.tpcms.android.ui.home.profile

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.databinding.UserProfileFragmentBinding
import com.ssas.tpcms.android.repo.home.HomeClickEvents
import com.ssas.tpcms.android.repo.home.HomeVM
import com.ssas.tpcms.android.ui.auth.LoginActivity
import com.ssas.tpcms.android.ui.home.HomeActivity
import com.ssas.tpcms.android.utils.Utils

/**
 * Created by Jaspal on 12/13/2019.
 */

private const val ARG_PROFILE_DATA = "OFFICER_CODE"

class UserProfileFragment : BaseBottomSheetFragment<UserProfileFragmentBinding, HomeVM>() {

    var data: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        arguments?.let {
            data = it.getString(ARG_PROFILE_DATA).toString()
        }
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.user_profile_fragment, HomeVM::class.java)

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
    }

    override fun subscribeToEvents(vm: HomeVM) {
        binding.vm = vm

        vm.clickEvents.observe(this, Observer {
            when (it) {
                HomeClickEvents.CANCLE_BUTTON_CLICK -> {
                    dismiss()
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(data: String) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROFILE_DATA, data)
                }
            }
    }
}