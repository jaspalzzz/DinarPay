package com.ssas.tpcms.android.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ssas.tpcms.android.utils.PermissionResultListener
import com.ssas.tpcms.android.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.ui.dialogs.ProgressCircularDialog
import com.ssas.tpcms.android.ui.dialogs.ProgressDialog
import com.ssas.tpcms.android.utils.ContextWrapper
import com.ssas.tpcms.android.utils.MyContextWrapper
import io.reactivex.annotations.Nullable

abstract class BaseActivitySimple : AppCompatActivity() {

    private lateinit var progressCirluarDialog: ProgressCircularDialog
    private lateinit var progressDialog: ProgressDialog

    val PERMISSION_REQUEST_CAMERA_CODE = 253
    val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE = 251
    lateinit var fusedLocationClient: FusedLocationProviderClient


    var CAMERA_PERMISSION =
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    var GALLARY_PERMISSION = arrayOf(Manifest.permission.CAMERA)

    var mPermissionResultListener: PermissionResultListener? = null

    fun setPermissionResultListener(mPermissionResultListener: PermissionResultListener) {
        this.mPermissionResultListener = mPermissionResultListener
    }

    override fun attachBaseContext(newBase: Context?) {
        var context = ContextWrapper.wrap(newBase!!, "ar")
        super.attachBaseContext(context)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressCirluarDialog = ProgressCircularDialog()
        progressDialog = ProgressDialog()
        setupGoogleClient()
    }

    private fun setupGoogleClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    fun transperantStatusBar() {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        } catch (exp: Exception) {
        }

    }

    fun showSnackbar(view: View, messsage: Int) {
        Snackbar.make(view, getString(messsage), Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbar(view: View, messsage: String) {
        Snackbar.make(view, messsage, Snackbar.LENGTH_SHORT).show()
    }

    fun snackBarAction(view: View, message: Int, clickListener: View.OnClickListener) {
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.retry), clickListener)
        snackbar.show()
    }


    fun alertDialogShow(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
        context: Context,
        message: String,
        okLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.ok), okLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogConfirmShow(
        context: Context,
        message: String,
        okLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.ok), okLister)
        builder.setNegativeButton(
            getString(R.string.cancel),
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogFullConfrimShow(
        context: Context,
        title: String,
        message: String,
        okButtonTitle: String,
        okLister: DialogInterface.OnClickListener,
        canelLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(okButtonTitle, okLister)
        builder.setNegativeButton(getString(R.string.cancel), canelLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
        context: Context,
        message: String,
        okButtonTitle: String,
        okLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton(okButtonTitle, okLister)
        builder.setNegativeButton(
            getString(R.string.cancel),
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean?) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) {
                item.isVisible = visible!!
            }
        }
    }

    fun showCircularProgress() {
        if (progressCirluarDialog != null) {
            progressCirluarDialog.show(supportFragmentManager, "")
        }
    }

    fun dismissCircularProgress() {
        if (progressCirluarDialog != null) {
            progressCirluarDialog.dismiss()
        }
    }

    fun showProgress() {
        if (progressDialog != null) {
            progressDialog.show(supportFragmentManager, "")
        }
    }

    fun dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss()
        }
    }

    fun setFocus(view: View) {
        Utils.hideKeyboardOnClick(this, view)
        view.requestFocus()
        //view.bringToFront()
    }

    fun hasCameraPermission(): Boolean {
        for (permission in CAMERA_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, CAMERA_PERMISSION,
            PERMISSION_REQUEST_CAMERA_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}