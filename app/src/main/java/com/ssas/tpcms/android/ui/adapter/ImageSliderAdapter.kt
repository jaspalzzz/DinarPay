package com.ssas.tpcms.android.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.ssas.tpcms.android.R

/**
 * Created by Jaspal on 12/20/2019.
 */

class ImageSliderAdapter(var context: Context, var imageList: ArrayList<Int>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var inflater = LayoutInflater.from(container.context)
        var view = inflater.inflate(R.layout.image_slider_layout, container, false)
        var sliderImageView = view.findViewById(R.id.sliderImageView) as ImageView
        sliderImageView.setImageResource(imageList[position])
        container.addView(view, 0)
        return view
    }


    override fun getCount(): Int {
        return imageList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}