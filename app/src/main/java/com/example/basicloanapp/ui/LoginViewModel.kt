package com.example.basicloanapp.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoanRepository

) : ViewModel() {
    private val disposables = CompositeDisposable()

    val validationResult = MutableLiveData<LoginValidation>(LoginValidation.DEFAULT)


    fun login(name: String, password: String) {
        disposables.add(
            repository.login(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        repository.saveTokenToSharedPrefs(it)
                        validationResult.value = LoginValidation.AUTHORIZED
                    },
                    {
                        validationResult.value = LoginValidation.USER_NOT_FOUND
                    })
        )
    }


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}

enum class LoginValidation(val message: String) {
    USER_NOT_FOUND("There is no such user or password incorrect."),
    NETWORK("Network or device problem, try again."),
    DEFAULT("Default"),
    AUTHORIZED("Authorized")
}
