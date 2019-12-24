package com.ssas.tpcms.android.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseFragment
import com.ssas.tpcms.android.data.models.auth.OfficerLoginResponse
import com.ssas.tpcms.android.databinding.FragmentLoginVerifyBinding
import com.ssas.tpcms.android.repo.auth.AuthClickEvents
import com.ssas.tpcms.android.repo.auth.AuthVM
import com.ssas.tpcms.android.ui.ConstantKeys
import com.ssas.tpcms.android.ui.home.HomeActivity
import com.ssas.tpcms.android.utils.Utils

/**
 * Created by Jaspal on 12/11/2019.
 */
class LoginVerifyFragment : BaseFragment<FragmentLoginVerifyBinding, AuthVM>() {

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_login_verify, AuthVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

        if (arguments != null) {
            var userid = arguments?.getString(ConstantKeys.USER_ID)
            viewModel.userIdString = userid.toString()
        }
    }

    override fun subscribeToEvents(vm: AuthVM) {

        resetObservers(vm)
        binding.vm = vm

        vm.networkError.observe(this, Observer {
            if (isLifeCycleResumed()) {
                if (it) {
                    alertDialogShow(
                        context!!,
                        getString(R.string.no_network_title),
                        getString(R.string.no_network_connection)
                    )
                }
            }
        })

        vm.userIdFirstDigit.observe(this, Observer {
            if (isLifeCycleResumed()) {
                if (it?.length == 1) {
                    binding.secondDigitet.requestFocus()
                } else if (it?.length == 0) {
                    binding.firstDigitet.clearFocus()
                    binding.firstDigitet.requestFocus()
                }
            }
        })
        vm.userIdSecondDigit.observe(this, Observer {
            if (isLifeCycleResumed()) {
                if (it?.length == 1) {
                    binding.thirdDigitet.requestFocus()
                } else if (it?.length == 0) {
                    binding.firstDigitet.requestFocus()
                }
            }
        })

        vm.userIdThirdDigit.observe(this, Observer {
            if (isLifeCycleResumed()) {
                if (it?.length == 1) {
                    Utils.hideKeyboardOnClick(context!!, binding.thirdDigitet)
                    enableVerifyButton()
                } else if (it?.length == 0) {
                    binding.secondDigitet.requestFocus()
                    disableVerifyButton()
                }
            }
        })

        vm.clickEvents.observe(this, Observer {
            if (isLifeCycleResumed()) {
                when (it) {
                    AuthClickEvents.LOGIN_PASSCODE_CLICK -> {
                        Utils.jumpActivity(context!!, HomeActivity::class.java)
                    }
                }
            }
        })

        vm.officerLoginResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    (context as LoginActivity).showCircularProgress()
                }
                Status.ERROR -> {
                    (context as LoginActivity).dismissCircularProgress()
                    var error = Utils.errorHandlingWithStatus(context!!, it.error)
                    alertDialogShow(context!!, error.message)
                    Log.e("jaspal", "Exception Error " + error)
                }
                Status.SUCCESS -> {
                    (context as LoginActivity).dismissCircularProgress()
                    var response = it.response
                    if (response.status?.responseCode == "OPS10002") {
                        if (response.status?.responseMsg.equals("Success")) {
                            handleResponsResult(response)
                        } else {
                            alertDialogShow(context!!, response.status?.responseValue.toString())
                        }
                    } else {
                        alertDialogShow(context!!, response.status?.responseValue.toString())
                    }
                }
            }
        })

    }

    private fun resetObservers(vm: AuthVM) {
        vm.networkError.removeObservers(this)
        vm.userIdFirstDigit.removeObservers(this)
        vm.userIdSecondDigit.removeObservers(this)
        vm.userIdThirdDigit.removeObservers(this)
        vm.clickEvents.removeObservers(this)
        vm.officerLoginResponse.removeObservers(this)
    }

    private fun handleResponsResult(response: OfficerLoginResponse) {
        var profileResponse = response.profileResponse
        var officerCode = response.officerCode
        var serializeProfilResponse = Gson().toJson(profileResponse)
        var bundle = Bundle()
        bundle.putString(
            ConstantKeys.ARG_PROFILE_DETAIL,
            serializeProfilResponse
        )
        bundle.putString(ConstantKeys.ARG_OFFICER_CODE, officerCode)

        (context as LoginActivity).saveLoginDetails(
            serializeProfilResponse,
            officerCode.toString()
        )
        Utils.jumpActivityWithData(context!!, HomeActivity::class.java, bundle)
        (context as LoginActivity).finish()
    }

    private fun enableVerifyButton() {
        binding.enableButton = true
    }

    private fun disableVerifyButton() {
        binding.enableButton = false
    }
}