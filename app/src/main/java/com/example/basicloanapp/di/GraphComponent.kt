package com.example.basicloanapp.di

import com.example.basicloanapp.MainActivity
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LoanServiceModule::class])
public interface GraphComponent {
    fun inject(mainActivity: MainActivity)
}