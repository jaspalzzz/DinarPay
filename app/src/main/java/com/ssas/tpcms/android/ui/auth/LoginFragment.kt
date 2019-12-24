package com.ssas.tpcms.android.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseFragment
import com.ssas.tpcms.android.databinding.FragmentLoginBinding
import com.ssas.tpcms.android.repo.auth.AuthClickEvents
import com.ssas.tpcms.android.repo.auth.AuthVM
import com.ssas.tpcms.android.ui.adapter.ImageSliderAdapter
import java.util.*

/**
 * Created by Jaspal on 12/11/2019.
 */

class LoginFragment : BaseFragment<FragmentLoginBinding, AuthVM>() {

    private lateinit var pagerAdapter: ImageSliderAdapter
    private var timer: Timer = Timer()
    private var imageHandler = Handler()
    private var currentPage = 0
    private var pageNumbers = DummyData.imageItems.size-1
    private val SLIDE_PERIOD: Long = 2500

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_login, AuthVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createImageSlider()
    }

    private fun createImageSlider() {
        pagerAdapter = ImageSliderAdapter(context!!, DummyData.imageItems)
        binding.sliderPager.adapter = pagerAdapter
        binding.sliderPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }
            @SuppressLint("MissingSuperCall")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPage=position
            }

        })
        startImageSlider()
    }

    private fun startImageSlider() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                imageHandler.post(updateImage)
            }
        }, SLIDE_PERIOD, SLIDE_PERIOD)
    }

     private var updateImage = Runnable {
        if (currentPage == pageNumbers) {
            currentPage = 0
        }
        binding.sliderPager.setCurrentItem(currentPage++,true)
    }

    override fun subscribeToEvents(vm: AuthVM) {
        binding.vm = vm
        vm.clickEvents.observe(this, Observer {
            when (it) {
                AuthClickEvents.LOGIN_LOGIN_CLICK -> {
                    (context as LoginActivity).moveLoginUserIdFragment()
                }
            }
        })
    }
}