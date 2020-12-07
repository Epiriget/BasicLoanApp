package com.example.basicloanapp.data

import android.content.SharedPreferences
import android.provider.SyncStateContract
import com.example.basicloanapp.service.*
import com.example.basicloanapp.util.Constants
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class LoanRepository @Inject constructor (private val service: LoanService,
    private val sharedPreferences: SharedPreferences) {
    fun login(name: String, password: String): Single<String> {
        return service.login(AuthRequest(name, password))
    }

    fun register(name: String, password: String): Single<RegistrationResponse> {
        return service.registration(AuthRequest(name, password))
    }

    fun getLoans(): Single<List<LoanBodyResponse>> {
        val token = getTokenFromSharedPrefs()
        return service.getAllLoans(token)
    }

    fun getTokenFromSharedPrefs(): String {
        return sharedPreferences.getString(Constants.PREFERENCES_BEARER_KEY, "")!!
    }

    fun saveTokenToSharedPrefs(token: String) {
        sharedPreferences.edit()
            .putString(Constants.PREFERENCES_BEARER_KEY, token)
            .apply()
    }

    fun getLoanConditions(): Single<LoanConditions> {
        return service.getLoanConditions(getTokenFromSharedPrefs())
    }

    fun createLoan(loan: LoanCreateRequest): Single<LoanBodyResponse> {
        val token = getTokenFromSharedPrefs()
        return service.createLoan(token, loan)
    }

    fun clearTokenFromSharedPrefs() {
        sharedPreferences.edit()
            .putString(Constants.PREFERENCES_BEARER_KEY, "")
            .apply()
    }
}