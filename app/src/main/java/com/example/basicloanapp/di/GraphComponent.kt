package com.example.basicloanapp.di

import android.app.Application
import android.content.Context
import com.example.basicloanapp.ui.*
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LoanRepositoryModule::class, ApplicationModule::class, ViewModelModule::class])
interface GraphComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(splashFragment: SplashFragment)
    fun inject(loanListFragment: LoanListFragment)
    fun inject(baseFragment: BaseFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(application: Application): Builder

        fun build(): GraphComponent
    }
}
