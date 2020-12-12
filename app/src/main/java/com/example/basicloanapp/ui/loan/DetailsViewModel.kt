package com.example.basicloanapp.ui.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.domain.LoanUseCase
import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.service.LoanBodyResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val useCase: LoanUseCase) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _loan = MutableLiveData<Loan>()
    val loan: LiveData<Loan> = _loan


    fun getLoan(id: Int) {
        disposables.add(
            useCase.getLoan(id)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _loan.postValue(it)
                    },
                    {
                        // Todo: implement error handling
                    })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}