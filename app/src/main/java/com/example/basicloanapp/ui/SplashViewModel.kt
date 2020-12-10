package com.example.basicloanapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.domain.AuthorizationUseCase
import javax.inject.Inject

class SplashViewModel @Inject constructor(useCase: AuthorizationUseCase): ViewModel() {
    val isAuthenticated = MutableLiveData<Boolean>()

    // Implementation for long term bearer key.
    // In another case add bearer key valid-state check.
    init {
        isAuthenticated.value = useCase.getToken().isNotEmpty()
    }
}

