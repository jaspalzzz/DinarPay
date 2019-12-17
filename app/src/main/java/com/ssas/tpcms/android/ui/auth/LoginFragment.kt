package com.ssas.tpcms.android.ui.auth

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseFragment
import com.ssas.tpcms.android.databinding.FragmentLoginBinding
import com.ssas.tpcms.android.repo.auth.AuthClickEvents
import com.ssas.tpcms.android.repo.auth.AuthVM

/**
 * Created by Jaspal on 12/11/2019.
 */

class LoginFragment : BaseFragment<FragmentLoginBinding, AuthVM>() {

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_login, AuthVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun subscribeToEvents(vm: AuthVM) {
        binding.vm = vm
        vm.clickEvents.observe(this, Observer {
            when(it){
                AuthClickEvents.LOGIN_LOGIN_CLICK->{
                    (context as LoginActivity).moveLoginUserIdFragment()
                }
            }
        })
    }
}