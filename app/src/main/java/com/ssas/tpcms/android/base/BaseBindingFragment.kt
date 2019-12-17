package com.ssas.tpcms.android.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseBindingFragment<T : ViewDataBinding> : BaseFragmentSimple() {
    var binding: T?=null
    var mContext: Context? = null
    var mActivity: Activity? = null

    abstract val fragmentBinding: FragmentBinding
    abstract fun onCreateViewFragment(savedInstanceState: Bundle?)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
        mActivity = context as Activity?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val fragmentBinding = fragmentBinding
            binding = DataBindingUtil.inflate(inflater, fragmentBinding.layoutResId, container, false)
            onCreateViewFragment(savedInstanceState)
        return binding?.root
    }

    inner class FragmentBinding(
        @param:LayoutRes @field:LayoutRes
        val layoutResId: Int
    )

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }




}