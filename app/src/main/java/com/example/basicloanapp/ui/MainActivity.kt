package com.example.basicloanapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.basicloanapp.LoanApplication
import com.example.basicloanapp.R
import com.example.basicloanapp.data.LoanRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: LoanRepository

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as LoanApplication).repositoryComponent.inject(this)


//        disposables.add(repository.login("PostmanTestName", "postman_pass")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {
//                    Log.d("MainActivity", "Success")
////                    text.text = it
//                },
//                {
//                    Log.d("MainActivity", "Error")
////                    text.text = it.message
//                }
//            ))
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
