package com.example.basicloanapp.util

import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.service.LoanBodyResponse

class DataConverter {
    companion object {
        fun convert(loan: LoanBodyResponse): Loan {
            return Loan(
                amount = loan.amount,
                date = TimeConverter.convertToDateWithTime(loan.date),
                firstName = loan.firstName,
                id = loan.id,
                lastName = loan.lastName,
                percent = loan.percent,
                period = loan.period,
                toDate = TimeConverter.convertToDateWithTime(loan.date, loan.period.toLong()),
                phoneNumber = loan.phoneNumber,
                state = loan.state
            )
        }

        fun convert(loans: List<LoanBodyResponse>): List<Loan> {
            return loans.map { convert(it) }
        }
    }
}