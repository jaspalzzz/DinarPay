package com.ssas.tpcms.android.widgets
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat


/*class TabTileView(context: Context?, attrs: AttributeSet?) : LinearLayout(context!!, attrs) {
    init {
        inflate(context, R.layout.tab_tile_layout,this)
        val imageView: ImageView = findViewById(R.id.img)
        val textView: TextView = this.findViewById(R.id.title)

        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.tabTileView)
        imageView.setImageDrawable(attributes?.getDrawable(R.styleable.tabTileView_tab_img))
        textView.text = attributes?.getString(R.styleable.tabTileView_tab_title)
        textView.setTextColor(attributes?.getColor(R.styleable.tabTileView_tab_text_color,ActivityCompat.getColor(getContext(),R.color.colorWhiteTrans))!!)
        attributes?.recycle()
    }
}*/
