package com.ssas.tpcms.android.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.auth.OfficersProfileResponseModel
import com.ssas.tpcms.android.data.models.qr.MissionOfficerResponse
import com.ssas.tpcms.android.databinding.MissionItemBinding
import com.ssas.tpcms.android.widgets.BaseRecyclerView
import java.util.ArrayList

/**
 * Created by Jaspal on 12/18/2019.
 */
class ProfileMissionListAdpter : BaseRecyclerView<ProfileMissionListAdpter.Holder>() {

    var missionList: ArrayList<MissionOfficerResponse> = ArrayList()
    var profileData: OfficersProfileResponseModel? = null

    class Holder(var binding: MissionItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun count(): Int {
        return missionList?.size
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<MissionItemBinding>(
            inflater,
            R.layout.mission_item,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolderMethod(holder: Holder, position: Int) {
        holder.binding.profileItem=profileData
        holder.binding.missionItem=missionList[position]
        holder.binding.executePendingBindings()
    }

    fun add(
        list: ArrayList<MissionOfficerResponse>,
        profileData: OfficersProfileResponseModel?
    ) {
        missionList = list
        this.profileData = profileData
        notifyDataSetChanged()
    }
}