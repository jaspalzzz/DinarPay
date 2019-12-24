package com.ssas.tpcms.android.ui.home.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.base.BaseDialogFragment
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.data.models.qr.MissionOfficerResponse
import com.ssas.tpcms.android.databinding.UserProfileFragmentBinding
import com.ssas.tpcms.android.repo.home.HomeClickEvents
import com.ssas.tpcms.android.repo.home.HomeVM

/**
 * Created by Jaspal on 12/13/2019.
 */

private const val ARG_PROFILE_DATA = "ARG_PROFILE_DATA"
private const val ARG_MISSION_DATA = "ARG_MISSION_DATA"

class UserProfileFragment : BaseBottomSheetFragment<UserProfileFragmentBinding, HomeVM>() {

    var data: String = ""
    private var missionList: ArrayList<MissionOfficerResponse>? = ArrayList()
    private lateinit var navController: NavController
    private lateinit var hostFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        arguments?.let {
            data = it.getString(ARG_PROFILE_DATA).toString()
            missionList = it.getSerializable(ARG_MISSION_DATA) as ArrayList<MissionOfficerResponse>?
        }
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.user_profile_fragment, HomeVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        hostFragment = fragmentManager?.findFragmentById(R.id.profile_nav_naviagtion)!!
        navController = NavHostFragment.findNavController(hostFragment)
        moveProfileFragment()
    }

    private fun moveProfileFragment() {
        navController.navigate(R.id.myProfileCardFragment, Bundle().apply {
            putString(ARG_PROFILE_DATA, data)
        })
    }

    private fun moveMissionFragment() {
        if (missionList!!.isNotEmpty()) {
            navController.navigate(R.id.myMissionCardFragment, Bundle().apply {
                putString(ARG_PROFILE_DATA, data)
                putSerializable(ARG_MISSION_DATA, missionList)
            })
        } else {
            alertDialogShow(context!!, getString(R.string.mission_error))
        }
    }

    override fun subscribeToEvents(vm: HomeVM) {
        binding.vm = vm

        vm.clickEvents.observe(this, Observer {
            when (it) {
                HomeClickEvents.CANCLE_BUTTON_CLICK -> {
                    dismiss()
                }
                HomeClickEvents.MY_PROFILE_CLICK -> {
                    moveProfileFragment()
                }
                HomeClickEvents.MY_PROFILE_MISSION_CLICK -> {
                    moveMissionFragment()
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(
            data: String,
            missionList: ArrayList<MissionOfficerResponse>?
        ) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROFILE_DATA, data)
                    putSerializable(ARG_MISSION_DATA, missionList)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (hostFragment != null) {
            fragmentManager?.beginTransaction()?.remove(hostFragment)?.commit()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }


}