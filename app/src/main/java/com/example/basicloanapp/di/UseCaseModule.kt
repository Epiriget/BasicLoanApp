package com.example.basicloanapp.di

import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.domain.AuthorizationUseCase
import com.example.basicloanapp.domain.LoanUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LoanRepositoryModule::class])
class UseCaseModule {
    @Provides
    @Singleton
    fun authUseCase(repository: LoanRepository): AuthorizationUseCase {
        return AuthorizationUseCase(repository)
    }

    @Provides
    @Singleton
    fun loanUseCase(repository: LoanRepository): LoanUseCase {
        return LoanUseCase(repository)
    }
}