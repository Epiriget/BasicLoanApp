package com.example.basicloanapp.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val repository: LoanRepository,
                private val sharedPreferences: SharedPreferences): ViewModel() {

    companion object {
        private const val TAG = "RegistrationVM"
    }

    private val bearerKey = sharedPreferences.getString(Constants.PREFERENCES_BEARER_KEY, "")
    private val disposables = CompositeDisposable()

    // Todo: make livedata fields private with public getters
    val isValid = MutableLiveData<Boolean>(false)
    val notValidDescription = MutableLiveData<ValidationResponse>(ValidationResponse.DEFAULT)

    fun register(name: String, password: String, repeatPassword: String) {
        if(validate(name, password, repeatPassword)) {
            disposables.add(repository.register(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        routeToLogin()
                    },
                    {
                        // Mock error text
                        if(it.message?.contains("400") == true) {
                            notValidDescription.value = ValidationResponse.ACCOUNT_ALREADY_EXISTS
                        } else {
                            notValidDescription.value = ValidationResponse.NETWORK
                        }
                        Log.d(TAG, it.message?: "")
                    }
                )
            )

        } else {
            notValidDescription.value = ValidationResponse.NOT_EQUAL_PASSWORDS
        }
    }

    private fun routeToLogin() {
        // Todo: implement routing to login page
        Log.d(TAG, "RouteToLogin")
    }

    private fun validate(name: String, password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}

enum class ValidationResponse(val message: String) {
    ACCOUNT_ALREADY_EXISTS("Account already exists!"),
    NOT_EQUAL_PASSWORDS("Passwords are not equal!"),
    NETWORK("Network or device problem, try again."),
    DEFAULT("default state"),
}