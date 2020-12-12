package com.example.basicloanapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.domain.AuthorizationUseCase
import com.example.basicloanapp.ui.validation.LoginValidation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val useCase: AuthorizationUseCase,
    private val state: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val NAME_KEY = "login.state.name"
        private const val PASSWORD_KEY = "login.state.password"
    }

    private val disposables = CompositeDisposable()

    private val _validationResult = MutableLiveData<LoginValidation>()
    val validationResult: LiveData<LoginValidation> = _validationResult

    val name: LiveData<String> = state.getLiveData(NAME_KEY)
    val password: LiveData<String> = state.getLiveData(PASSWORD_KEY)

    fun login(name: String, password: String) {
        _validationResult.value = LoginValidation.AWAITING
        val preValidation = validateInput(name, password)
        if(preValidation == LoginValidation.GOOD) {
            disposables.add(
                useCase.login(name, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            useCase.saveToken(it)
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

    fun saveState(name: String?, password: String?) {
        state.apply {
            set(NAME_KEY, name)
            set(PASSWORD_KEY, password)
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
