package com.example.basicloanapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanConditions
import com.example.basicloanapp.service.LoanCreateRequest
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateLoanViewModel @Inject constructor(private val repository: LoanRepository) :
    ViewModel() {
    private val disposables = CompositeDisposable()

    private val _conditions = MutableLiveData<LoanConditions>()
    val conditions: LiveData<LoanConditions>
        get() = _conditions

    private val _validationResult = MutableLiveData<CreateValidation>()
    val validationResult: LiveData<CreateValidation>
        get() = _validationResult

    init {
        getConditions()
    }

    private fun getConditions() {
        disposables.add(
            repository.getLoanConditions()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _conditions.postValue(it)
                    },
                    {
                        Log.d("CreateLoan", "Error in retrieving conditions:" + it.message)
                    })
        )
    }

    fun createLoan(
        amount: String, name: String, surname: String,
        percent: Double, period: Int, phone: String
    ) {
        val preValidation = validateInput(amount, name, surname, phone)
        if (preValidation == CreateValidation.GOOD) {
            val loan = LoanCreateRequest(amount.toInt(), name, surname, percent, period, phone)
            disposables.add(
                repository.createLoan(loan)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _validationResult.value = CreateValidation.GOOD
                        },
                        {
                            _validationResult.postValue(CreateValidation.NETWORK)
                        })
            )
        } else {
            _validationResult.value = preValidation
        }
    }

    private fun validateInput(
        amount: String,
        name: String,
        surname: String,
        phone: String
    ): CreateValidation {
        val amountInt: Int? = amount.toIntOrNull()
        return when {
            amountInt == null -> CreateValidation.AMOUNT_TYPE_ERROR
            amountInt > conditions.value!!.maxAmount -> CreateValidation.AMOUNT_SIZE_ERROR
            name.isEmpty() -> CreateValidation.NAME_EMPTY
            surname.isEmpty() -> CreateValidation.SURNAME_EMPTY
            phone.isEmpty() -> CreateValidation.PHONE_EMPTY
            else -> CreateValidation.GOOD
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}


enum class CreateValidation(val message: String) {
    NETWORK("Error on server side, try again."),
    AMOUNT_TYPE_ERROR("Amount is empty!"),
    AMOUNT_SIZE_ERROR("Amount is more than let in conditions!"),
    NAME_EMPTY("Name is empty or consists of whitespaces!"),
    SURNAME_EMPTY("Surname is empty or consists of whitespaces!"),
    PHONE_EMPTY("Phone number is empty or consists of whitespaces!"),
    GOOD("Good")
}
