package com.ssas.tpcms.android.utils

interface PermissionResultListener {
    fun onPermissionResult(
        requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray
    )
}
