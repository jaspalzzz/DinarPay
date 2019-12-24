package com.ssas.tpcms.android.widgets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.base.ItemClickListener
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileRecordModel
import com.ssas.tpcms.android.data.models.qr.MissionOfficerResponse
import java.util.ArrayList

/**
 * Created by Jaspal on 12/10/2019.
 */
abstract class BaseRecyclerView<V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {

    lateinit var listener: ItemClickListener

    abstract fun count(): Int
    abstract fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): V
    abstract fun onBindViewHolderMethod(holder: V, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        return onCreateHolderMethod(parent, viewType)
    }

    override fun getItemCount(): Int {
        return count()
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClick(this, position)
        }
        onBindViewHolderMethod(holder, position)
    }

    fun setItemClickListener(owner: ItemClickListener) {
        if (owner == null) {
            return
        }
        listener = owner
    }

}