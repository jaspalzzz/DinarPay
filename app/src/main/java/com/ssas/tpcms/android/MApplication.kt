package com.ssas.tpcms.android

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModelProvider
import com.ssas.tpcms.android.di.components.AppComponent
import com.ssas.tpcms.android.di.components.DaggerAppComponent
import com.ssas.tpcms.android.di.modules.AppModule
import com.ssas.tpcms.android.utils.MyContextWrapper
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.*


class MApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        provider =
            ViewModelProvider.AndroidViewModelFactory(this) as ViewModelProvider.NewInstanceFactory
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        lateinit var instance: MApplication
        lateinit var provider: ViewModelProvider.NewInstanceFactory
        lateinit var appComponent: AppComponent
    }
}