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

class CreateLoanViewModel @Inject constructor(private val repository: LoanRepository):
    ViewModel() {
    private val disposables = CompositeDisposable()

    private val _conditions = MutableLiveData<LoanConditions>()
    val conditions: LiveData<LoanConditions>
        get() = _conditions

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

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

    fun createLoan(loan: LoanCreateRequest) {
        disposables.add(repository.createLoan(loan)
            .subscribeOn(Schedulers.io())
            .subscribe({

            }, {
                _errorText.postValue(it.message)
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}