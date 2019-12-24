package com.ssas.tpcms.android.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseFragment
import com.ssas.tpcms.android.databinding.FragmentLoginUseridBinding
import com.ssas.tpcms.android.repo.auth.AuthClickEvents
import com.ssas.tpcms.android.repo.auth.AuthVM
import com.ssas.tpcms.android.utils.Utils

/**
 * Created by Jaspal on 12/11/2019.
 */
class LoginUserIdFragment : BaseFragment<FragmentLoginUseridBinding, AuthVM>() {


    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_login_userid, AuthVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {
        binding.firstDigitet.requestFocus()
    }

    override fun subscribeToEvents(vm: AuthVM) {
        resetObserbers(vm)
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
                    binding.fourthDigitet.requestFocus()
                } else if (it?.length == 0) {
                    binding.secondDigitet.requestFocus()
                }
            }
        })

        vm.userIdFourthDigit.observe(this, Observer {
            if (isLifeCycleResumed()) {
                if (it?.length == 1) {
                    binding.fifthDigitet.requestFocus()
                } else if (it?.length == 0) {
                    binding.thirdDigitet.requestFocus()
                }
            }
        })

        vm.userIdFiveDigit.observe(this, Observer {
            if (isLifeCycleResumed()) {
                if (it?.length == 1) {
                    //Utils.hideKeyboardOnClick(context!!, binding.fifthDigitet)
                    enableVerifyButton()
                } else if (it?.length == 0) {
                    binding.fourthDigitet.requestFocus()
                    disableVerifyButton()
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
                            var userId = vm.getUserId()
                            (context as LoginActivity).moveUserPasscodeFragment(userId)
                        } else {
                            alertDialogShow(context!!, response.status?.responseValue.toString())
                        }
                    } else {
                        alertDialogShow(context!!, response.status?.responseValue.toString())
                    }
                }
            }
        })

        vm.clickEvents.observe(this, Observer {
            if(isLifeCycleResumed()){
                when (it) {
                    AuthClickEvents.LOGIN_VERIFY_USERID_CLICK -> {
                    }
                }
            }
        })
    }

    private fun resetObserbers(vm: AuthVM) {
        vm.networkError.removeObservers(this)
        vm.userIdFirstDigit.removeObservers(this)
        vm.userIdSecondDigit.removeObservers(this)
        vm.userIdThirdDigit.removeObservers(this)
        vm.userIdFourthDigit.removeObservers(this)
        vm.userIdFiveDigit.removeObservers(this)
        vm.clickEvents.removeObservers(this)
        vm.officerLoginResponse.removeObservers(this)
    }

    private fun enableVerifyButton() {
        binding.enableButton = true
    }

    private fun disableVerifyButton() {
        binding.enableButton = false
    }

}