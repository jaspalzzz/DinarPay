package com.ssas.tpcms.android.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Jaspal on 12/18/2019.
 */
class RatioLayoutML(
    context: Context?, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) :
    LinearLayoutManager(context, orientation, reverseLayout) {
    var ratio = 0.9f

    private val horizontalSpace get() = width - paddingStart - paddingEnd

    private val verticalSpace get() = width - paddingTop - paddingBottom

    override fun generateDefaultLayoutParams() =
        scaledLayoutParams(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?) =
        scaledLayoutParams(super.generateLayoutParams(lp))

    override fun generateLayoutParams(c: Context?, attrs: AttributeSet?) =
        scaledLayoutParams(super.generateLayoutParams(c, attrs))

    private fun scaledLayoutParams(layoutParams: RecyclerView.LayoutParams) =
        layoutParams.apply {
            when (orientation) {
                HORIZONTAL -> width = (horizontalSpace * ratio + 0.5).toInt()
                VERTICAL -> height = (verticalSpace * ratio + 0.5).toInt()
            }
        }
}