package com.ssas.tpcms.android.ui.auth

import android.os.Bundle
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseActivity
import com.ssas.tpcms.android.databinding.ActivityVerifuDeviceIdBinding
import com.ssas.tpcms.android.repo.auth.AuthVM

class VerifuDeviceIdActivity : BaseActivity<ActivityVerifuDeviceIdBinding, AuthVM>() {

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_verifu_device_id,
            AuthVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {

    }

    override fun subscribeToEvents(vm: AuthVM) {

    }
}
