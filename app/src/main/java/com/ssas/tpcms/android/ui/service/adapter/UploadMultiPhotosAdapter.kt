package com.ssas.tpcms.android.ui.service.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.models.UploadImageModel
import com.ssas.tpcms.android.databinding.UploadPhotoItemBinding
import com.ssas.tpcms.android.utils.files.PickMediaHelper

/**
 * Created by Jaspal on 12/12/2019.
 */
class UploadMultiPhotosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ADD_MORE_VIEW = 0
    private val NORMAL_VIEW = 1
    lateinit var listner: ImageItemClickListener
    var imageList: ArrayList<UploadImageModel> = ArrayList()

    class Holder(var binding: UploadPhotoItemBinding) : RecyclerView.ViewHolder(binding.root)

    class AddHolder(var itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ADD_MORE_VIEW
        } else {
            NORMAL_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        return if (viewType == ADD_MORE_VIEW) {
            AddHolder(inflater.inflate(R.layout.upload_more_photo_item, parent, false))
        } else {
            var binding = DataBindingUtil.inflate<UploadPhotoItemBinding>(
                inflater,
                R.layout.upload_photo_item,
                parent,
                false
            )
            Holder(binding)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ADD_MORE_VIEW) {
            holder.itemView.setOnClickListener {
                listner.onAddMoreImageClick(this)
            }
        } else {
            (holder as Holder).binding.item = imageList.get(position)

            (holder as Holder).binding.deleteImage.setOnClickListener {
                listner.onRemoveImageClick(position)
            }
            (holder as Holder).binding.root.setOnClickListener {
                listner.onImageViewClick(this, position)
            }
            (holder as Holder).binding.executePendingBindings()
        }
    }

    fun initAddMoreOption() {
        imageList.add(UploadImageModel(0, "", ""))
    }

    fun getImageListSize(): Int {
        return imageList.size
    }

    fun addImage(item: UploadImageModel) {
        imageList.add(item)
        notifyDataSetChanged()
    }

    fun removeImage(position: Int) {
        if (position > 0) {
            imageList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun getUploadImageList(): ArrayList<UploadImageModel> {
        return imageList
    }

    interface ImageItemClickListener {
        fun onAddMoreImageClick(uploadMultiPhotosAdapter: UploadMultiPhotosAdapter)
        fun onRemoveImageClick(position: Int)
        fun onImageViewClick(
            uploadMultiPhotosAdapter: UploadMultiPhotosAdapter,
            position: Int
        )
    }

    fun addImageItemClickListener(owner: ImageItemClickListener) {
        listner = owner
    }
}