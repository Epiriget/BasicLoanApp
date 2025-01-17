package com.example.basicloanapp.domain.entity

data class Loan(
    val amount: Int,
    val date: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val toDate: String,
    val phoneNumber: String,
    val state: String
    )