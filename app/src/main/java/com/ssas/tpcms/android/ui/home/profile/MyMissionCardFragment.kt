package com.ssas.tpcms.android.ui.home.profile

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseFragment
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.data.models.qr.MissionOfficerResponse
import com.ssas.tpcms.android.databinding.FragmentProfileMissionBinding
import com.ssas.tpcms.android.repo.home.HomeVM
import com.ssas.tpcms.android.ui.home.adapter.ProfileMissionListAdpter
import com.ssas.tpcms.android.widgets.RatioLayoutML

/**
 * Created by Jaspal on 12/18/2019.
 */

private const val ARG_PROFILE_DATA = "ARG_PROFILE_DATA"
private const val ARG_MISSION_DATA = "ARG_MISSION_DATA"

class MyMissionCardFragment : BaseFragment<FragmentProfileMissionBinding, HomeVM>() {

    var data: String = ""
    private lateinit var missionAdapter: ProfileMissionListAdpter
    var profileData: OfficersProfileResponseModel? = null
    var missionList: ArrayList<MissionOfficerResponse>? = ArrayList()

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_profile_mission, HomeVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun subscribeToEvents(vm: HomeVM) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inflateMissionList()
        arguments?.let {
            data = it.getString(ARG_PROFILE_DATA).toString()
            if (data.isNotEmpty()) {
                profileData = Gson().fromJson<OfficersProfileResponseModel>(
                    data,
                    OfficersProfileResponseModel::class.java
                )
            }
        }
        missionList = arguments?.getSerializable(ARG_MISSION_DATA) as ArrayList<MissionOfficerResponse>?
        missionAdapter.add(missionList!!,profileData)
    }

    private fun inflateMissionList() {
        missionAdapter = ProfileMissionListAdpter()
        createHorizontalList(binding.missionList).adapter = missionAdapter
    }

    private fun createHorizontalList(recyclerView: RecyclerView): RecyclerView {
        //var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        var layoutManager = RatioLayoutML(context, LinearLayoutManager.HORIZONTAL, false)
        var snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = layoutManager
        return recyclerView
    }
}