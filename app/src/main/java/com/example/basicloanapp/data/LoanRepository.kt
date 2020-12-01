package com.example.basicloanapp.data

import com.example.basicloanapp.service.LoanService
import com.example.basicloanapp.service.LoginRequest
import com.example.basicloanapp.service.LoginResponse
import io.reactivex.Single
import javax.inject.Inject

class LoanRepository @Inject constructor (private val service: LoanService) {
    fun login(name: String, password: String): Single<LoginResponse> {
        return service.login(LoginRequest(name, password))
    }
}