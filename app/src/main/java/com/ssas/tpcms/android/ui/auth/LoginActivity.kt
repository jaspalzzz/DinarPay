package com.ssas.tpcms.android.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ssas.tpcms.android.MApplication
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBindingActivity
import com.ssas.tpcms.android.data.prefs.PrefKeys
import com.ssas.tpcms.android.data.prefs.PrefMain
import com.ssas.tpcms.android.databinding.ActivityLoginBinding
import com.ssas.tpcms.android.ui.ConstantKeys
import javax.inject.Inject

class LoginActivity : BaseBindingActivity<ActivityLoginBinding>() {

    private lateinit var navController: NavController
    @Inject
    lateinit var prefMain: PrefMain

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_login)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        MApplication.appComponent.inject(this@LoginActivity)
        init()
        initToolbar()
        deviceIdAuthChecking()
    }

    private fun deviceIdAuthChecking() {
        if (prefMain[PrefKeys.DEVICE_ID, ""].isNullOrEmpty()) {
            moveVerifyDeviceIDFragment()
        } else {
            moveLoginFragment()
        }
    }

    private fun initToolbar() {
        binding.toolbar.backIconBt.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        navController = Navigation.findNavController(this@LoginActivity, R.id.loginAuthNavigation)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> {
                    binding.toolbar.toolbar.visibility = View.GONE
                }
                R.id.loginUserIdFragment -> {
                    setToolbarTitle(R.string.verify_user_text)
                }
                R.id.loginVerifyFragment -> {
                    setToolbarTitle(R.string.verify_account_text)
                }
            }
        }
    }

    private fun setToolbarTitle(title: Int) {
        binding.toolbar.toolbar.visibility = View.VISIBLE
        binding.toolbar.toolbarTitle.setText(title)
    }

    fun moveVerifyDeviceIDFragment() {
        navController.navigate(R.id.verifyDerviceIDFragment)
    }

    fun moveLoginFragment() {
        navController.navigate(R.id.loginFragment)
    }


    fun moveLoginUserIdFragment() {
        navController.navigate(R.id.loginUserIdFragment)
    }

    fun saveLoginDetails(profileDetail: String, officerCode: String) {
        prefMain.put(PrefKeys.OFFICER_PROFILE, profileDetail)
        prefMain.put(PrefKeys.OFFICER_CODE, officerCode)
    }

    fun saveDeviceID(deviceId: String) {
        prefMain.put(PrefKeys.DEVICE_ID, deviceId)
    }

    fun moveUserPasscodeFragment(userId: String) {
        var bundle = Bundle()
        bundle.putString(ConstantKeys.USER_ID, userId)
        navController.navigate(R.id.loginVerifyFragment, bundle)
    }
}
