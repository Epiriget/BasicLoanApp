package com.example.basicloanapp.data

import com.example.basicloanapp.service.LoanService
import com.example.basicloanapp.service.AuthRequest
import com.example.basicloanapp.service.RegistrationResponse
import io.reactivex.Single
import javax.inject.Inject

class LoanRepository @Inject constructor (private val service: LoanService) {
    fun login(name: String, password: String): Single<String> {
        return service.login(AuthRequest(name, password))
    }

    fun register(name: String, password: String): Single<RegistrationResponse> {
        return service.registration(AuthRequest(name, password))
    }
}