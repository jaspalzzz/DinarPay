package com.ssas.tpcms.android.ui.home.profile

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseFragment
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.databinding.FragmentMyprofileCardBinding
import com.ssas.tpcms.android.repo.home.HomeVM

/**
 * Created by Jaspal on 12/18/2019.
 */


private const val ARG_PROFILE_DATA = "ARG_PROFILE_DATA"

class MyProfileCardFragment : BaseFragment<FragmentMyprofileCardBinding, HomeVM>() {
    var data: String = ""

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_myprofile_card, HomeVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun subscribeToEvents(vm: HomeVM) {
        binding.vm = vm
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            data = it.getString(ARG_PROFILE_DATA).toString()
        }
        if (data.isNotEmpty()) {
            var profileData = Gson().fromJson<OfficersProfileResponseModel>(
                data,
                OfficersProfileResponseModel::class.java
            )
            binding.item = profileData
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(data: String) =
            MyProfileCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROFILE_DATA, data)
                }
            }
    }

}