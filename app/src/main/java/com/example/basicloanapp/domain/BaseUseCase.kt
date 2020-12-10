package com.example.basicloanapp.domain

import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.data.LoanRepository_Factory
import javax.inject.Inject

open class BaseUseCase @Inject constructor(private val repository: LoanRepository) {
    fun saveToken(token: String) {
        repository.saveTokenToSharedPrefs(token)
    }

    fun clearToken() {
        repository.clearTokenFromSharedPrefs()
    }

    fun getToken(): String {
        return repository.getTokenFromSharedPrefs()
    }
}