package com.ssas.tpcms.android.ui.service.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileRecordModel
import com.ssas.tpcms.android.databinding.CriminalDbItemBinding
import com.ssas.tpcms.android.widgets.BaseRecyclerView
import com.ssas.tpcms.android.widgets.FooterRecyclerView
import java.util.ArrayList

/**
 * Created by Jaspal on 12/11/2019.
 */
class CriminalDbAdapter : FooterRecyclerView() {

    var criminalProfileList = ArrayList<CrimnalProfileRecordModel>()
    val ITEM_VIEW = 1


    class Holder(var binding: CriminalDbItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun addData(list: ArrayList<CrimnalProfileRecordModel>) {
        criminalProfileList.addAll(list)
        notifyDataSetChanged()
    }

    override fun count(): Int {
        return criminalProfileList.size
    }

    override fun viewType(): Int {
        return ITEM_VIEW
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<CriminalDbItemBinding>(
            inflater,
            R.layout.criminal_db_item,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolderMethod(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.binding.item = criminalProfileList.get(position)
            holder.binding.root.setOnClickListener {
                listener.onItemClick(this, position)
            }
            holder.binding.executePendingBindings()
        }
    }

    fun getItem(position: Int) : CrimnalProfileRecordModel{
        return criminalProfileList.get(position)
    }

    fun clearData() {
        criminalProfileList.clear()
        notifyDataSetChanged()
    }
}