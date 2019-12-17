package com.ssas.tpcms.android.ui.service.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.criminal_record.ImageModel
import com.ssas.tpcms.android.databinding.CasePhotoItemBinding
import com.ssas.tpcms.android.widgets.BaseRecyclerView

/**
 * Created by Jaspal on 12/12/2019.
 */
class CasePhotosAdapter(var imageList: ArrayList<ImageModel>) :
    BaseRecyclerView<CasePhotosAdapter.Holder>() {

    class Holder(var binding: CasePhotoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun count(): Int {
        return imageList.size
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<CasePhotoItemBinding>(
            inflater,
            R.layout.case_photo_item,
            parent,
            false
        )
        return Holder(
            binding
        )
    }

    override fun onBindViewHolderMethod(holder: Holder, position: Int) {
        if (!TextUtils.isEmpty(imageList[position].image)) {
            holder.binding.item = this.imageList.get(position)
        }
        holder.binding.executePendingBindings()
    }
}