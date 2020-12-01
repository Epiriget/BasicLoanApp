package com.example.basicloanapp

import android.app.Application
import com.example.basicloanapp.di.*

class LoanApplication: Application() {
    public lateinit var repositoryComponent: GraphComponent
    override fun onCreate() {
        super.onCreate()
        repositoryComponent = DaggerGraphComponent.builder()
            .loanServiceModule(LoanServiceModule())
            .build()
    }
}