package com.example.basicloanapp.util

import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.service.LoanBodyResponse
import org.junit.Test

class DataConverterTest {
    companion object {
        private val loan = Loan(200, "08.12.2020 14:47", "SomeFirstName", 42,
            "SomeLastName", 12.21, 31, "08.01.2021 14:47",
            "8924232352", "REGISTERED")
        private val loanBodyResponse = LoanBodyResponse(200, "2020-12-08T07:47:04.228+00:00",
            "SomeFirstName",42, "SomeLastName", 12.21,
            31, "8924232352", "REGISTERED")

        val loans = listOf(loan, loan.copy(id = 43), loan.copy(amount = 100))
        val loanBodyResponses = listOf(loanBodyResponse, loanBodyResponse.copy(id = 43), loanBodyResponse.copy(amount = 100))
    }

    @Test
    fun `convert single loanBodyResponse to loan`() {
        val expected = loan

        val actual = DataConverter.convert(loanBodyResponse)

        assert(expected == actual)
    }
    @Test
    fun `convert list of loanBodyResponse to loan list`() {
        val expected = loans

        val actual = DataConverter.convert(loanBodyResponses)

        assert(expected == actual)
    }
}