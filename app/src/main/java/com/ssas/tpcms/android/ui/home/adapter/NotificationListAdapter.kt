package com.ssas.tpcms.android.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.notification.NotificationDataModel
import com.ssas.tpcms.android.databinding.NotificationItemBinding
import com.ssas.tpcms.android.utils.DateUtils
import com.ssas.tpcms.android.widgets.BaseRecyclerView
import com.ssas.tpcms.android.widgets.FooterRecyclerView
import java.util.ArrayList

/**
 * Created by Jaspal on 12/11/2019.
 */
class NotificationListAdapter : FooterRecyclerView() {
    val ITEM_VIEW = 1
    var notificationList: ArrayList<NotificationDataModel> = ArrayList()

    class Holder(var binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun count(): Int {
        return notificationList.size
    }

    override fun viewType(): Int {
        return ITEM_VIEW
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<NotificationItemBinding>(
            inflater,
            R.layout.notification_item,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolderMethod(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.binding.item = notificationList.get(position)
            holder.binding.executePendingBindings()
        }
    }

    fun addData(list: ArrayList<NotificationDataModel>) {
        notificationList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        notificationList.clear()
        notifyDataSetChanged()
    }

}