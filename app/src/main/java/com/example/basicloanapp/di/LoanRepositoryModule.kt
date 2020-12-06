package com.example.basicloanapp.di

import android.content.SharedPreferences
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [LoanServiceModule::class, ApplicationModule::class])
class LoanRepositoryModule {
    @Singleton
    @Provides
    fun repository(service: LoanService, sharedPreferences: SharedPreferences): LoanRepository {
        return LoanRepository(service, sharedPreferences)
    }
}