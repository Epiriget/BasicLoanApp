package com.example.basicloanapp.domain

import com.example.basicloanapp.TestSchedulerRule
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanService
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
        whenever(service.login(any())).thenReturn(Single.just("Bearer 123"))
        val repository = LoanRepository(service, mock())

//        whenever(repository.login(eq("name"), eq("pasSword")))
//            .thenReturn(Single.just("Bearer 123"))

        val useCase = AuthorizationUseCase(repository)

        val actual = useCase.login(eq("name"), eq("pasSword"))

        val expected = "Bearer 123"

//        verify(repository).login("name", "pasSword")
        actual
            .test()
            .assertValue(expected)
    }
}