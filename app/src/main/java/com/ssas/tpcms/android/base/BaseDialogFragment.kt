package com.ssas.tpcms.android.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import com.ssas.tpcms.android.MApplication

abstract class BaseDialogFragment<T : ViewDataBinding, V : AndroidViewModel> : DialogFragment() {
    lateinit var binding: T
    lateinit var viewModel: V
    lateinit var mContext: Context

    abstract val dialogBinding: DialogBinding


    abstract fun onCreateDialogFragment(savedInstanceState: Bundle?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStart() {
        super.onStart()
        /*val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogBinding = dialogBinding
        binding = DataBindingUtil.inflate(inflater, dialogBinding.layoutResId, container, false)
        viewModel = MApplication.provider.create(dialogBinding.clazz)
        subscribeToEvents(viewModel)
        onCreateDialogFragment(savedInstanceState)
        return binding.root
    }

    protected abstract fun subscribeToEvents(vm: V)

    inner class DialogBinding(
        @param:LayoutRes @field:LayoutRes
        val layoutResId: Int, val clazz: Class<V>
    )

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    fun isLifeCycleResumed() = viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED

}
