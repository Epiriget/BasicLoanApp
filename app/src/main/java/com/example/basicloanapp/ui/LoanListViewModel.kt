package com.example.basicloanapp.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.service.LoanBodyResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoanListViewModel @Inject constructor(
    private val repository: LoanRepository
) : ViewModel() {

    private val _loans = MutableLiveData<List<LoanBodyResponse>>()
    val loans: LiveData<List<LoanBodyResponse>>
        get() = _loans

    private val disposables = CompositeDisposable()

    fun getLoans() {
        disposables.add(repository.getLoans()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    _loans.postValue(it)
                }, {
                    Log.d("LoanList", "Error in getLoans()")
                })
        )
    }

    fun logOut() {
        repository.clearTokenFromSharedPrefs()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}