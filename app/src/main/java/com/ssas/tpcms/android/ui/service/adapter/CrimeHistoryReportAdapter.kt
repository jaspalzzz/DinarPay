package com.ssas.tpcms.android.ui.service.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.crime_report.CrimeReportDataItem
import com.ssas.tpcms.android.databinding.CrimeHistoryReportItemBinding
import com.ssas.tpcms.android.widgets.BaseRecyclerView
import com.ssas.tpcms.android.widgets.FooterRecyclerView
import java.util.ArrayList

/**
 * Created by Jaspal on 12/11/2019.
 */

class CrimeHistoryReportAdapter : FooterRecyclerView() {

    val ITEM_VIEW = 1

    var crimeReportList: ArrayList<CrimeReportDataItem> = ArrayList()

    class Holder(var binding: CrimeHistoryReportItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun count(): Int {
        return crimeReportList.size
    }

    override fun viewType(): Int {
        return ITEM_VIEW
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<CrimeHistoryReportItemBinding>(
            inflater,
            R.layout.crime_history_report_item,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolderMethod(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.binding.item = crimeReportList.get(position)
            holder.binding.root.setOnClickListener {
                listener.onItemClick(this, position)
            }
            holder.binding.executePendingBindings()
        }
    }

    fun addData(list: ArrayList<CrimeReportDataItem>) {
        crimeReportList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        crimeReportList.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): CrimeReportDataItem {
        return crimeReportList.get(position)
    }
}