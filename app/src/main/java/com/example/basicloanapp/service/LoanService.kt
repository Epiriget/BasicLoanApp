package com.example.basicloanapp.service

import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.*

interface LoanService {
    @POST("login")
    fun login(
        @Body request: AuthRequest
    ): Single<String>

    @POST("registration")
    fun registration(
        @Body request: AuthRequest
    ): Single<RegistrationResponse>

    @POST("loans")
    fun createLoan(
        @Header("Authorization") bearerToken: String,
        @Body request: LoanCreateRequest
    ): Single <LoanBodyResponse>

    @GET("loans/{id}")
    fun getLoanById(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: Int
    ): Single<LoanBodyResponse>

    @GET("loans/all")
    fun getAllLoans(
        @Header("Authorization") bearerToken: String
        ): Single<List<LoanBodyResponse>>

    @GET("loans/conditions")
    fun getLoanConditions(
        @Header("Authorization") bearerToken: String
    ): Single<LoanConditions>


}

data class RegistrationResponse(val name: String, val role: String)

data class AuthRequest(val name: String, val password: String)

data class LoanCreateRequest(
    val amount: Int,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String
)

data class LoanBodyResponse(
    val amount: Int,
    val date: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val state: String
)

data class LoanConditions(
    val maxAmount: Int,
    val percent: Double,
    val period: Int
)

