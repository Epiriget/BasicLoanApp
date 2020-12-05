package com.example.basicloanapp.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.util.Constants
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoanRepository,
                                                private val sharedPreferences: SharedPreferences
): ViewModel() {
    private val disposables = CompositeDisposable()

    val validationResult = MutableLiveData<LoginValidation>(LoginValidation.DEFAULT)


    fun login(name: String, password: String) {
        disposables.add(repository.login(name, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                sharedPreferences.edit()
                    .putString(Constants.PREFERENCES_BEARER_KEY, it)
                    .apply()
                mockRouteToList()
            }, {
                validationResult.value = LoginValidation.USER_NOT_FOUND
            })
        )
    }

    fun mockRouteToList(){
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}

enum class LoginValidation(val message: String) {
    USER_NOT_FOUND("There is no such user!"),
    NETWORK("Network or device problem, try again."),
    DEFAULT("Default")
}
