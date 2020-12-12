package com.example.basicloanapp.domain

import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.RegistrationResponse
import io.reactivex.Single
import javax.inject.Inject

open class AuthorizationUseCase @Inject constructor(private val repository: LoanRepository) :
    BaseUseCase(repository) {

    fun register(name: String, password: String): Single<RegistrationResponse> {
        return repository.register(name, password)
    }

    fun login(name: String, password: String): Single<String> {
        return repository.login(name, password)
    }
}