package com.ssas.tpcms.android.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.service.ServiceListModel
import com.ssas.tpcms.android.databinding.ServiceItemBinding
import com.ssas.tpcms.android.widgets.BaseRecyclerView

/**
 * Created by Jaspal on 12/11/2019.
 */
class ServiceGridAdapter : BaseRecyclerView<ServiceGridAdapter.Holder>() {
    var serviceList = ArrayList<ServiceListModel>()

    class Holder(var binding: ServiceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun count(): Int {
        return serviceList.size
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<ServiceItemBinding>(
            inflater,
            R.layout.service_item,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolderMethod(holder: Holder, position: Int) {
        holder.binding.item = serviceList.get(position)
        holder.binding.executePendingBindings()
    }

    fun addData(list: ArrayList<ServiceListModel>) {
        serviceList = list
        notifyDataSetChanged()
    }
}