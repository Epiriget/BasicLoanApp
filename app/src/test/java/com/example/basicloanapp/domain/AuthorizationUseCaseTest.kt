package com.example.basicloanapp.domain

import android.content.SharedPreferences
import com.example.basicloanapp.TestSchedulerRule
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanService
import com.example.basicloanapp.util.Constants
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class AuthorizationUseCaseTest {
    @get:Rule
    val testRule = TestSchedulerRule()

    @Test
    fun `login positive result`() {
        val service: LoanService = mock()
        val sharedPrefs: SharedPreferences = mock()
        whenever(service.login(any())).thenReturn(Single.just("Bearer 123"))
        whenever(sharedPrefs.getString(eq(Constants.PREFERENCES_BEARER_KEY), any()))
            .thenReturn("Bearer 123")
        val repository = LoanRepository(service, sharedPrefs)

        val useCase = AuthorizationUseCase(repository)

        val actual = useCase.login("name", "pasSword")

        val expected = "Bearer 123"

        verify(service).login(any())
        actual
            .test()
            .assertValue(expected)
    }
}