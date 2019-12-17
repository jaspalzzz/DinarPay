package com.ssas.tpcms.android.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.utils.countrycode.Country
import com.ssas.tpcms.android.utils.countrycode.CountryUtils
import java.io.File
import java.io.InputStream


object BindingImageAdapter {

    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter(value = ["src_image"], requireAll = false)
    fun setImageDrawable(imageView: ImageView, country: Country) {
        val image = CountryUtils.getFlagDrawableResId(country)
        imageView.setImageResource(image)
    }

    @JvmStatic
    @BindingAdapter(value = ["base_image"])
    fun setImageBase64(imageView: ImageView, encodedImage: String?) {
        if(!encodedImage.isNullOrEmpty()){
            val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            Glide.with(imageView.context).load(decodedByte).into(imageView)
        }
    }

    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: String?) {
        if (imageUri == null) {
            view.setImageURI(null)
        } else {
            view.setImageURI(Uri.parse(imageUri))
        }
    }

    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: Uri) {
        view.setImageURI(imageUri)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageDrawable(view: ImageView, drawable: Drawable) {
        view.setImageDrawable(drawable)
    }

    @JvmStatic
    @BindingAdapter("imageResource")
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("imageFileResource")
    fun setImageFileResource(imageView: ImageView, resource: String?) {
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             var uri: Uri? = null
             if (Build.VERSION.SDK_INT > 24) {
                 uri = FileProvider.getUriForFile(
                     imageView.context,
                     imageView.context.getString(R.string.app_id),
                     File(resource)!!
                 )
             } else {
                 uri = Uri.fromFile(File(resource))
             }

             var resolver = imageView.context.contentResolver
             resolver.openInputStream(uri).use { inputStream: InputStream? ->
                 var bitmap = BitmapFactory.decodeStream(inputStream)
                 imageView.setImageBitmap(bitmap)
             }*//*
        } else {*/
        if (resource != null) {
            Glide.with(imageView.context)
                .load(resource)
                .into(imageView)
            // }
        }
    }

    @JvmStatic
    @BindingAdapter("imageRoundCorner")
    fun setRoundCornerImage(imageView: ImageView, drawable: Drawable) {
        val circularProgressDrawable = CircularProgressDrawable(imageView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 5f
        circularProgressDrawable.start()

        val requestOptions = RequestOptions()
            .placeholder(circularProgressDrawable)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
        //.diskCacheStrategy(DiskCacheStrategy.ALL)*/

        Glide.with(imageView.context)
            .load(drawable)
            .apply(requestOptions)
            .into(imageView)
    }

}