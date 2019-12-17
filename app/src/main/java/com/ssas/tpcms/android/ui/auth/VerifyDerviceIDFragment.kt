package com.ssas.tpcms.android.ui.auth

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseFragment
import com.ssas.tpcms.android.databinding.ActivityVerifuDeviceIdBinding
import com.ssas.tpcms.android.repo.auth.AuthClickEvents
import com.ssas.tpcms.android.repo.auth.AuthVM

/**
 * Created by Jaspal on 12/14/2019.
 */

class VerifyDerviceIDFragment : BaseFragment<ActivityVerifuDeviceIdBinding, AuthVM>() {
    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.activity_verifu_device_id, AuthVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun subscribeToEvents(vm: AuthVM) {
        binding.vm = vm

        vm.deviceIdError.observe(this, Observer {
            alertDialogShow(context!!, getString(it))
        })

        vm.clickEvents.observe(this, Observer {
            when (it) {
                AuthClickEvents.SUMBIT_DEVICEID_CLICK -> {
                    (context as LoginActivity).saveDeviceID(vm.getDeviceId())
                    (context as LoginActivity).moveLoginFragment()
                }
            }
        })
    }
}