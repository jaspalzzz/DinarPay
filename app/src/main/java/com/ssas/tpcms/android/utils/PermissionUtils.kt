package com.ssas.tpcms.android.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionUtils {

    const val PERMISSION_REQUEST_CAMERA_CODE = 253
    const val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE = 251
    const val PERMISSION_LOCATION_CODE = 254

    var CAMERA_PERMISSION=arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
    var GALLARY_PERMISSION=arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    var LOCATION_PERMISSION =arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)

    @JvmStatic
    fun hasCameraPermission(context: Context?,  permissions: Array<String>): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    @JvmStatic
    fun requestPermissions(context: Activity, arrayPermission: Array<String>, REQUEST_CODE: Int) {
        ActivityCompat.requestPermissions(
            context, arrayPermission,
            REQUEST_CODE
        )
    }
}
