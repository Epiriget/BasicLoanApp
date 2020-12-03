package com.example.basicloanapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.LoanApplication
import com.example.basicloanapp.data.LoanRepository
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(val repository: LoanRepository): ViewModel() {
    val isValid = MutableLiveData<Boolean>(false)
    
    // Todo: Replace mock login implementation
    fun login(name: String, password: String, repeatPassword: String) {
        if(password == repeatPassword) {
            isValid.value = true
        }
        else {
            isValid.value = false
        }
    }
}