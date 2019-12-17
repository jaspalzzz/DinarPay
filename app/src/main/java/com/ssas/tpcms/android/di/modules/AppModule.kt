package com.ssas.tpcms.android.di.modules

import com.ssas.tpcms.android.MApplication
import com.ssas.tpcms.android.data.prefs.PrefMain
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MApplication) {

    @Singleton
    @Provides
    fun providePrefMain(): PrefMain {
        return PrefMain(application.applicationContext)
    }
}
