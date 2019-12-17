package com.ssas.tpcms.android.ui.service.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.crimetype.CrimeTypeResponse
import com.ssas.tpcms.android.databinding.CrimeTypeItemBinding
import com.ssas.tpcms.android.widgets.BaseRecyclerView

/**
 * Created by Jaspal on 12/17/2019.
 */

class CrimeTypeAdapter(var crimeTypeList: ArrayList<CrimeTypeResponse>) :
    BaseRecyclerView<CrimeTypeAdapter.Holder>() {


    class Holder(var binding: CrimeTypeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun count(): Int {
        return crimeTypeList.size
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<CrimeTypeItemBinding>(
            inflater,
            R.layout.crime_type_item,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolderMethod(holder: Holder, position: Int) {
        holder.binding.item= crimeTypeList.get(position)
        holder.binding.executePendingBindings()
    }

    fun getitem(position: Int):CrimeTypeResponse{
        return crimeTypeList.get(position)
    }

}