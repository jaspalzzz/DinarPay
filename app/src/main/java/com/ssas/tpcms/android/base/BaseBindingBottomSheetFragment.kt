package com.ssas.tpcms.android.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.ssas.tpcms.android.MApplication
import com.ssas.tpcms.android.ui.dialogs.ProgressCircularDialog
import com.ssas.tpcms.android.ui.dialogs.ProgressDialog


abstract class BaseBindingBottomSheetFragment<T : ViewDataBinding> :
    BottomSheetDialogFragment() {

    lateinit var binding: T
    var mContext: Context? = null
    var mActivity: Activity? = null
    private lateinit var progressCirluarDialog: ProgressCircularDialog
    private lateinit var progressDialog: ProgressDialog

    abstract val fragmentBinding: FragmentBinding
    abstract fun onCreateViewFragment(savedInstanceState: Bundle?)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
        mActivity = context as Activity?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = fragmentBinding
        binding = DataBindingUtil.inflate(inflater, fragmentBinding.layoutResId, container, false)
        onCreateViewFragment(savedInstanceState)
        initProgress()
        return binding.root
    }

    fun initProgress() {
        progressCirluarDialog = ProgressCircularDialog()
        progressDialog = ProgressDialog()
    }

    inner class FragmentBinding(
        @param:LayoutRes
        val layoutResId: Int
    )

    fun showSnackbar(view: View, messsage: Int) {
        Snackbar.make(view, getString(messsage), Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbar(view: View, messsage: String) {
        Snackbar.make(view, messsage, Snackbar.LENGTH_SHORT).show()
    }

    fun snackBarAction(view: View, message: Int, clickListener: View.OnClickListener) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Retry", clickListener)
        snackbar.show()
    }


    fun alertDialogShow(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton("Ok") { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton("Ok") { dialogInterface, i -> dialogInterface.dismiss() }
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
        builder.setPositiveButton("Ok", okLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
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
        builder.setNegativeButton("cancel", canelLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean?) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) {
                item.isVisible = visible!!
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }


    fun hideKeyboard(activity: Activity) {
        val inputManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun showCircularProgress() {
        if (progressCirluarDialog != null) {
            progressCirluarDialog.show(fragmentManager, "")
        }
    }

    fun dismissCircularProgress() {
        if (progressCirluarDialog != null) {
            progressCirluarDialog.dismiss()
        }
    }

    fun showProgress() {
        if (progressDialog != null) {
            progressDialog.show(fragmentManager, "")
        }
    }

    fun dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss()
        }
    }
}