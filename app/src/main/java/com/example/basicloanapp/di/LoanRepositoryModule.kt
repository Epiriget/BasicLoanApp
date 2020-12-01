package com.example.basicloanapp.di

import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [LoanServiceModule::class])
class LoanRepositoryModule {
    @Singleton
    @Provides
    fun repository(service: LoanService): LoanRepository {
        return LoanRepository(service)
    }
}