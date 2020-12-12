package com.example.basicloanapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.domain.LoanUseCase
import com.example.basicloanapp.service.LoanConditions
import com.example.basicloanapp.service.LoanCreateRequest
import com.example.basicloanapp.ui.validation.CreateValidation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateLoanViewModel @Inject constructor(
    private val useCase: LoanUseCase,
    private val state: SavedStateHandle
) :
    ViewModel() {
    private val disposables = CompositeDisposable()

    private val _conditions = MutableLiveData<LoanConditions>()
    val conditions: LiveData<LoanConditions> = _conditions

    private val _validationResult = MutableLiveData<CreateValidation>()
    val validationResult: LiveData<CreateValidation> = _validationResult

    val name: LiveData<String> = state.getLiveData(NAME_KEY)
    val surname: LiveData<String> = state.getLiveData(SURNAME_KEY)
    val amount: LiveData<String> = state.getLiveData(AMOUNT_KEY)
    val phone: LiveData<String> = state.getLiveData(PHONE_KEY)

    companion object {
        private const val AMOUNT_KEY = "create.state.amount"
        private const val NAME_KEY = "create.state.name"
        private const val SURNAME_KEY = "create.state.surname"
        private const val PHONE_KEY = "create.state.phone"
    }


    init {
        getConditions()
    }

    private fun getConditions() {
        disposables.add(
            useCase.getLoanConditions()
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
                useCase.createLoan(loan)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _validationResult.postValue(CreateValidation.GOOD)
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
        val amountLong: Long? = amount.toLongOrNull()
        return when {
            amountLong == null -> CreateValidation.AMOUNT_TYPE_ERROR
            amountLong > conditions.value!!.maxAmount -> CreateValidation.AMOUNT_SIZE_ERROR
            name.isEmpty() -> CreateValidation.NAME_EMPTY
            surname.isEmpty() -> CreateValidation.SURNAME_EMPTY
            phone.isEmpty() -> CreateValidation.PHONE_EMPTY
            else -> CreateValidation.GOOD
        }
    }

    fun saveState(amount:String, name: String, surname: String, phone: String) {
        state.apply {
            set(AMOUNT_KEY, amount)
            set(NAME_KEY, name)
            set(SURNAME_KEY, surname)
            set(PHONE_KEY, phone)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}

