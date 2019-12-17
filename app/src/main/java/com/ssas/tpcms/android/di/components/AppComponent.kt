package com.ssas.tpcms.android.di.components
import com.ssas.tpcms.android.di.modules.AppModule
import com.ssas.tpcms.android.repo.auth.AuthVM
import com.ssas.tpcms.android.repo.home.HomeVM
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.auth.LoginActivity
import com.ssas.tpcms.android.ui.home.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(loginActivity: LoginActivity)
    fun inject(authVM: AuthVM)
    fun inject(homeVM: HomeVM)
    fun inject(serviceVM: ServiceVM)
    fun inject(homeActivity: HomeActivity)
}
