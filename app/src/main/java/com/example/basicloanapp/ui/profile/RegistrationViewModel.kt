package com.example.basicloanapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.R
import com.example.basicloanapp.domain.AuthorizationUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val useCase: AuthorizationUseCase,
    private val state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val NAME_KEY = "registration.state.name"
        private const val PASSWORD_KEY = "registration.state.password"
        private const val REPEAT_PASSWORD_KEY = "registration.state.repeatPassword"
    }

    private val disposables = CompositeDisposable()

    val name: LiveData<String> = state.getLiveData(NAME_KEY)
    val password: LiveData<String> = state.getLiveData(PASSWORD_KEY)
    val repeatPassword: LiveData<String> = state.getLiveData(REPEAT_PASSWORD_KEY)

    private val _validationResult = MutableLiveData<RegisterValidation>()
    val validationResult: LiveData<RegisterValidation> = _validationResult

    fun register(name: String, password: String, repeatPassword: String) {
        _validationResult.value = RegisterValidation.AWAITING
        val preValidation = validateInput(name, password, repeatPassword)
        if (preValidation == RegisterValidation.GOOD) {
            disposables.add(useCase.register(name, password)
                .subscribeOn(Schedulers.io())
                .flatMap { useCase.login(name, password) }
                .subscribe(
                    {
                        useCase.saveToken(it)
                        _validationResult.postValue(RegisterValidation.GOOD)
                    },
                    {
                        if (it.message?.contains("400") == true) {
                            _validationResult.postValue(RegisterValidation.ACCOUNT_ALREADY_EXISTS)
                        } else {
                            _validationResult.postValue(RegisterValidation.NETWORK)
                        }
                    }
                )
            )
        } else {
            _validationResult.value = preValidation
        }
    }

    private fun validateInput(
        name: String,
        password: String,
        repeatPassword: String
    ): RegisterValidation {
        return when {
            name.isEmpty() -> RegisterValidation.NAME_EMPTY
            password.isEmpty() -> RegisterValidation.PASSWORD_EMPTY
            repeatPassword.isEmpty() -> RegisterValidation.REPEAT_PASSWORD_EMPTY
            password != repeatPassword -> RegisterValidation.NOT_EQUAL_PASSWORDS
            else -> RegisterValidation.GOOD
        }
    }

    fun saveState(name: String?, password: String?, repeatPassword: String?) {
        state.apply {
            set(NAME_KEY, name)
            set(PASSWORD_KEY, password)
            set(REPEAT_PASSWORD_KEY, repeatPassword)
        }
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}

enum class RegisterValidation(val message: Int) {
    AWAITING(R.string.validation_awaiting),
    ACCOUNT_ALREADY_EXISTS(R.string.register_validation_account_exists),
    NOT_EQUAL_PASSWORDS(R.string.register_validation_not_equal_passwords),
    NETWORK(R.string.validation_network),
    NAME_EMPTY(R.string.validation_name_empty),
    PASSWORD_EMPTY(R.string.validation_password_empty),
    REPEAT_PASSWORD_EMPTY(R.string.validation_password_empty),
    GOOD(R.string.validation_good)
}