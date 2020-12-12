package com.example.basicloanapp.data

import android.content.SharedPreferences
import com.example.basicloanapp.TestSchedulerRule
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanConditions
import com.example.basicloanapp.service.LoanService
import com.example.basicloanapp.util.Constants
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers


class LoanRepositoryTest {
    companion object {
        val conditions = LoanConditions(2000, 13.31, 40)
    }

    @get:Rule
    val testRule = TestSchedulerRule()

    @Test
    fun `get loan conditions`() {
        val service: LoanService = mock()
        val sharedPrefs: SharedPreferences = mock()
        whenever(service.getLoanConditions(eq("Bearer 123"))).thenReturn(Single.just(conditions))
        whenever(sharedPrefs.getString(eq(Constants.PREFERENCES_BEARER_KEY), any()))
            .thenReturn("Bearer 123")
        val repository = LoanRepository(service, sharedPrefs)

        val actual = repository.getLoanConditions()

        val expected = conditions

        actual
            .test()
            .assertValue(expected)
    }

    @Test
    fun `login positive`() {
        val service: LoanService = mock()
        val sharedPrefs: SharedPreferences = mock()
        whenever(service.login(any())).thenReturn(Single.just("Bearer 123"))
        whenever(sharedPrefs.getString(eq(Constants.PREFERENCES_BEARER_KEY), any()))
            .thenReturn("Bearer 123")
        val repository = LoanRepository(service, sharedPrefs)

        val actual = repository.login("SomeName", "SomeSurname")

        val expected = "Bearer 123"

        actual
            .test()
            .assertValue(expected)
    }
    @Test
    fun `get token from shared prefs`() {
        val sharedPrefs: SharedPreferences = mock()
        whenever(sharedPrefs.getString(eq(Constants.PREFERENCES_BEARER_KEY), eq(""))).thenReturn("Bearer 123")
        val repository = LoanRepository(mock(), sharedPrefs)

        val actual = repository.getTokenFromSharedPrefs()

        val expected = "Bearer 123"

        assertEquals(actual, expected)
    }
}