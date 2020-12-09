package com.example.basicloanapp.ui

import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val repository: LoanRepository,
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
        val preValidation = validateInput(name, password, repeatPassword)
        if (preValidation == RegisterValidation.GOOD) {
            disposables.add(repository.register(name, password)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
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

enum class RegisterValidation(val message: String) {
    ACCOUNT_ALREADY_EXISTS("Account already exists!"),
    NOT_EQUAL_PASSWORDS("Passwords are not equal!"),
    NETWORK("Network or device problem, try again."),
    NAME_EMPTY("Name is empty or consists of whitespaces!"),
    PASSWORD_EMPTY("Password is empty or consists of whitespaces!"),
    REPEAT_PASSWORD_EMPTY("Password is empty or consists of whitespaces!"),
    GOOD("Good")
}