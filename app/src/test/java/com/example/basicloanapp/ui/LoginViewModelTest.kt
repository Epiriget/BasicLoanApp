package com.example.basicloanapp.ui

import android.content.SharedPreferences
import com.example.basicloanapp.TestSchedulerRule
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.di.ViewModelFactory
import com.example.basicloanapp.domain.AuthorizationUseCase
import com.example.basicloanapp.service.LoanService
import com.example.basicloanapp.util.Constants
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString


@Module
object TestViewModelModule {

    val viewModelFactory: ViewModelFactory = mock()

    @JvmStatic
    @Provides
    fun provideFavoritesViewModelFactory(): ViewModelFactory {
        return viewModelFactory
    }
}

class LoginViewModelTest {
    @get:Rule
    val testRule = TestSchedulerRule()

    @Test
    fun getValidationResult() {
        val service: LoanService = mock()
        val sharedPrefs: SharedPreferences = mock()
        whenever(service.login(any())).thenReturn(Single.just("Bearer 123"))
        whenever(sharedPrefs.getString(eq(Constants.PREFERENCES_BEARER_KEY), any()))
            .thenReturn("Bearer 123")
        val repository = LoanRepository(service, sharedPrefs)

        val useCase = AuthorizationUseCase(repository)

        val viewModel = LoginViewModel(useCase, mock())
        whenever(TestViewModelModule.viewModelFactory.create(LoginViewModel::class.java)).thenReturn(viewModel)

//        viewModel.login("SomeName", "SomeSurname")
    }

    @Test
    fun getName() {
    }

    @Test
    fun getPassword() {
    }

    @Test
    fun login() {
    }
}