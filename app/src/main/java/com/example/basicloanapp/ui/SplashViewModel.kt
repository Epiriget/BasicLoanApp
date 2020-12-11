package com.example.basicloanapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.domain.AuthorizationUseCase
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val useCase: AuthorizationUseCase): ViewModel() {

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    // Implementation for long term bearer key.
    // In another case add bearer key valid-state check.
    init {
        authenticate()
    }

    fun authenticate() {
        _isAuthenticated.value = useCase.getToken().isNotEmpty()
    }
}

