package com.example.basicloanapp.data

import android.content.SharedPreferences
import android.provider.SyncStateContract
import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.service.*
import com.example.basicloanapp.util.Constants
import com.example.basicloanapp.util.DataConverter
import com.example.basicloanapp.util.TimeConverter
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

open class LoanRepository @Inject constructor (private val service: LoanService,
    private val sharedPreferences: SharedPreferences) {
    fun login(name: String, password: String): Single<String> {
        return service.login(AuthRequest(name, password))
    }

    fun register(name: String, password: String): Single<RegistrationResponse> {
        return service.registration(AuthRequest(name, password))
    }

    fun getLoans(): Single<List<Loan>> {
        val token = getTokenFromSharedPrefs()
        return service.getAllLoans(token).map { DataConverter.convert(it) }
    }

    fun getLoanConditions(): Single<LoanConditions> {
        return service.getLoanConditions(getTokenFromSharedPrefs())
    }

    fun createLoan(loan: LoanCreateRequest): Single<Loan> {
        val token = getTokenFromSharedPrefs()
        return service.createLoan(token, loan).map { DataConverter.convert(it) }
    }

    fun getLoan(id: Int): Single<Loan> {
        return service.getLoanById(getTokenFromSharedPrefs(), id).map { DataConverter.convert(it) }
    }

    fun getTokenFromSharedPrefs(): String {
        return sharedPreferences.getString(Constants.PREFERENCES_BEARER_KEY, "")!!
    }

    fun saveTokenToSharedPrefs(token: String) {
        sharedPreferences.edit()
            .putString(Constants.PREFERENCES_BEARER_KEY, token)
            .apply()
    }

    fun clearTokenFromSharedPrefs() {
        sharedPreferences.edit()
            .putString(Constants.PREFERENCES_BEARER_KEY, "")
            .apply()
    }
}