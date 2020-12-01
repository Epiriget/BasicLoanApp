package com.example.basicloanapp.service

import com.google.gson.JsonArray
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface LoanService {
//    @Headers("Accept: application/json")
    @Headers("Accept: text/plain")
    @POST("login")
    fun login(
    @Body
    request: LoginRequest
    ):Single<LoginResponse>
}

data class LoginResponse(val authString: String)

data class LoginRequest(val name: String, val password: String)

