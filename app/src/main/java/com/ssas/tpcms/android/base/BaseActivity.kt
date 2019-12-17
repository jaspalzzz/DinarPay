package com.ssas.tpcms.android.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ssas.tpcms.android.MApplication
import io.reactivex.annotations.Nullable


abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : BaseActivitySimple() {

    lateinit var binding: T
    lateinit var viewModel: V
    abstract val bindingActivity: ActivityBinding
    abstract fun onCreateActivity(savedInstanceState: Bundle?)

    protected abstract fun subscribeToEvents(vm: V)

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityBinding = bindingActivity
        binding = DataBindingUtil.setContentView(this, activityBinding.layoutResId)
        viewModel = MApplication.provider.create(activityBinding.clazz)
        onCreateActivity(savedInstanceState)
        subscribeToEvents(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    inner class ActivityBinding(
        @param:LayoutRes @field:LayoutRes
        val layoutResId: Int, val clazz: Class<V>
    )
}