package com.ssas.tpcms.android.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.data.ErrorResponse
import com.ssas.tpcms.android.network.EmptyResultException
import com.ssas.tpcms.android.network.SoapFaulException
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    @Synchronized
    fun <T> jumpActivity(context: Context, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivityForResult(context: Activity, resultCode: Int, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        context.startActivityForResult(intent, resultCode)
    }

    @Synchronized
    fun <T> jumpActivityWithData(context: Context, clazz: Class<T>, bundle: Bundle) {
        val intent = Intent(context, clazz)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivityWithAction(context: Context, clazz: Class<T>, action: String) {
        val intent = Intent(context, clazz)
        intent.action = action
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivityForResult(
        context: Activity,
        resultCode: Int,
        clazz: Class<T>,
        bundle: Bundle
    ) {
        val intent = Intent(context, clazz)
        intent.putExtras(bundle)
        context.startActivityForResult(intent, resultCode)
    }


    fun switchFragment(
        clearStack: Boolean,
        fm: FragmentManager,
        frame: Int,
        fragment: Fragment,
        isStacked: Boolean
    ) {
        val tag = fragment.javaClass.simpleName

        if (clearStack) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        val transaction = fm.beginTransaction()
        transaction.replace(frame, fragment, tag)
        if (isStacked) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    fun hideSoftKeyBoard(context: Activity) {
        context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    fun hideKeyboardOnClick(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    fun modelToString(`object`: Any): String {
        val gson = Gson()
        return gson.toJson(`object`)
    }

    fun <T> stringToModel(json: String, clazz: Class<T>): Any {
        val gson = Gson()
        return gson.fromJson(json, clazz)!!
    }

    fun dateToString(date: String): String {
        var outDate: Date? = null
        val formatIn = SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss")
        val formatOut = SimpleDateFormat("dd/mm/yyyy hh:mm:ss a")
        try {
            outDate = formatIn.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatOut.format(outDate)
    }

    fun convertDateToMillisec(date: String): String {
        var loadingDatamili: String = ""
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        try {
            val mDate = sdf.parse(date)
            loadingDatamili = mDate.time.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return loadingDatamili
    }

    fun isInternet(context: Context): Boolean {
        return ConnectivityReceiver.isNetworkAvailable(context)
    }

    fun getSoftButtonsBarSizePort(activity: Activity): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            activity.windowManager.defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight)
                realHeight - usableHeight
            else
                0
        }
        return 0
    }

    fun errorHandlingWithStatus(context: Context, e: Throwable): ErrorResponse {
        var errorResponse = ErrorResponse(
            context.getString(R.string.exception_msg), ""
        )
        if (e is SoapFaulException) {
            errorResponse.message = e.message.toString()
        } else if (e is EmptyResultException) {
            errorResponse.message = e.message.toString()
        }
        return errorResponse
    }

    fun pxToDp(px: Int, context: Context): Int {
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun hidePassword(et: EditText) {
        et.transformationMethod = HideReturnsTransformationMethod.getInstance()
        et.setSelection(et.text!!.length)
    }

    fun showPassword(et: EditText) {
        et.transformationMethod = PasswordTransformationMethod()
        et.setSelection(et.text!!.length)
    }

    fun generateQRCode(encodeString: String): Bitmap? {
        return try {
            var barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(encodeString, BarcodeFormat.QR_CODE, 650, 650)
        } catch (e: Exception) {
            null
        }
    }

    fun generateBase64Image(path: String): String {
        var bitmap = BitmapFactory.decodeFile(path)
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var byteArray = baos.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun fetchAddressFromLocation(context: Context, location: Location): String {
        var geocoder = Geocoder(context, Locale.getDefault())
        var addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        if (addressList.isNullOrEmpty()) {
            return ""
        }

        var address = addressList[0].getAddressLine(0)
        var city = addressList[0].locality ?: ""
        var state = addressList[0].adminArea ?: ""
        var country = addressList[0].countryName ?: ""
        //var postalCode = addressList[0].postalCode

        var completeAddress = ""
        if (!TextUtils.isEmpty(address)) {
            completeAddress += address
        }
        if (!TextUtils.isEmpty(city)) {
            completeAddress += ", $city"
        }
        if (!TextUtils.isEmpty(state)) {
            completeAddress += ", $state"
        }
        if (!TextUtils.isEmpty(country)) {
            completeAddress += ", $country"
        }
        return completeAddress
    }

    /* fun getGreetingMessage(context: Context): String {
         val c = Calendar.getInstance()
         val timeOfDay = c.get(Calendar.HOUR_OF_DAY)
         return when (timeOfDay) {
             in 0..11 -> context.getString(R.string.good_morning)
             in 12..15 -> context.getString(R.string.good_afternoon)
             in 16..20 -> context.getString(R.string.good_evening)
             in 21..23 -> context.getString(R.string.good_nigh)
             else -> {
                 context.getString(R.string.hello)
             }
         }
     }*/

}
