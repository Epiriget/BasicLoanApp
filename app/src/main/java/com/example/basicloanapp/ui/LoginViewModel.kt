package com.example.basicloanapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoanRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _validationResult = MutableLiveData<LoginValidation>()
    val validationResult: LiveData<LoginValidation>
        get() = _validationResult


    fun login(name: String, password: String) {
        val preValidation = validateInput(name, password)
        if(preValidation == LoginValidation.GOOD) {
            disposables.add(
                repository.login(name, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            repository.saveTokenToSharedPrefs(it)
                            _validationResult.value = LoginValidation.GOOD
                        },
                        {
                            _validationResult.value = LoginValidation.USER_NOT_FOUND
                        })
            )
        } else {
            _validationResult.value = preValidation
        }
    }

    private fun validateInput(name: String, password: String): LoginValidation {
        return when {
            name.isEmpty() -> LoginValidation.NAME_EMPTY
            password.isEmpty() -> LoginValidation.PASSWORD_EMPTY
            else -> LoginValidation.GOOD
        }
    }


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}

enum class LoginValidation(val message: String) {
    USER_NOT_FOUND("There is no such user or password incorrect."),
    NAME_EMPTY("Name is empty or consists of whitespaces!"),
    PASSWORD_EMPTY("Password is empty or consists of whitespaces!"),
    NETWORK("Network or device problem, try again."),
    GOOD("Good")
}
