package com.example.basicloanapp.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val repository: LoanRepository): ViewModel() {

    private val disposables = CompositeDisposable()

    private val _validationResult = MutableLiveData<RegisterValidation>()
    val validationResult: LiveData<RegisterValidation>
        get() = _validationResult

    fun register(name: String, password: String, repeatPassword: String) {
        if(validate(name, password, repeatPassword)) {
            disposables.add(repository.register(name, password)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _validationResult.postValue(RegisterValidation.GOOD)

                    },
                    {
                        if(it.message?.contains("404") == true) {
                            _validationResult.postValue(RegisterValidation.ACCOUNT_ALREADY_EXISTS)
                        } else {
                            _validationResult.postValue(RegisterValidation.NETWORK)
                        }
                    }
                )
            )
        } else {
            _validationResult.value = RegisterValidation.NOT_EQUAL_PASSWORDS
        }
    }

    private fun validate(name: String, password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}

enum class RegisterValidation(val message: String) {
    ACCOUNT_ALREADY_EXISTS("Account already exists!"),
    NOT_EQUAL_PASSWORDS("Passwords are not equal!"),
    NETWORK("Network or device problem, try again."),
    GOOD("Good")
}