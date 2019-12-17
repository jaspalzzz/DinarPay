package com.ssas.tpcms.android.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Created by Jaspal on 12/14/2019.
 */
object SeviceUtilAdapter {
    @JvmStatic
    @BindingAdapter(value = ["first_name", "last_name"])
    fun timeTOGreeting(textView: TextView, fname: String?, lname: String?) {
        var fullName = fname
        textView.setText(fullName)
    }

    @JvmStatic
    @BindingAdapter(value = ["generate_qrCode"])
    fun qrFromData(imageView: ImageView, encodeString: String?) {
        if (encodeString != null) {
            var bitmap = Utils.generateQRCode(encodeString)
            imageView.setImageBitmap(bitmap)
        }
    }
}