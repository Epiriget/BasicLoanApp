package com.example.basicloanapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanBodyResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val repository: LoanRepository) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _loan = MutableLiveData<LoanBodyResponse>()
    val loan: LiveData<LoanBodyResponse>
        get() = _loan


    fun getLoan(id: Int) {
        disposables.add(
            repository.getLoan(id)
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