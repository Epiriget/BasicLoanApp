package com.example.basicloanapp.di

import com.example.basicloanapp.LoanApplication
import com.example.basicloanapp.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LoanRepositoryModule::class, ApplicationModule::class])
interface GraphComponent {
    fun inject(mainActivity: MainActivity)
}