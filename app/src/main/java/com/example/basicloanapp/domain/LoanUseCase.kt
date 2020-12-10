package com.example.basicloanapp.domain

import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.service.LoanBodyResponse
import com.example.basicloanapp.service.LoanConditions
import com.example.basicloanapp.service.LoanCreateRequest
import io.reactivex.Single
import javax.inject.Inject

class LoanUseCase @Inject constructor(private val repository: LoanRepository) :
    BaseUseCase(repository) {
    fun getLoan(id: Int): Single<Loan> {
        return repository.getLoan(id)
    }

    fun getLoans(): Single<List<Loan>> {
        return repository.getLoans()
    }

    fun createLoan(loan: LoanCreateRequest): Single<Loan> {
        return repository.createLoan(loan)
    }

    fun getLoanConditions(): Single<LoanConditions> {
        return repository.getLoanConditions()
    }
}