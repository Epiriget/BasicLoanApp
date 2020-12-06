package com.example.basicloanapp.data

import android.content.SharedPreferences
import android.provider.SyncStateContract
import com.example.basicloanapp.service.LoanService
import com.example.basicloanapp.service.AuthRequest
import com.example.basicloanapp.service.LoanBodyResponse
import com.example.basicloanapp.service.RegistrationResponse
import com.example.basicloanapp.util.Constants
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
        val token = sharedPreferences.getString(Constants.PREFERENCES_BEARER_KEY, "")!!
        return service.getAllLoans(token)
    }
}