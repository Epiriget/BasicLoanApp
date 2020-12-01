package com.example.basicloanapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.basicloanapp.service.LoanService
import com.example.basicloanapp.service.LoginRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var service: LoanService

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as LoanApplication).repositoryComponent.inject(this)


        disposables.add(service.login(LoginRequest("PostmanTestName", "postman_pass"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("MainActivity", "Success")
                    text.text = it.authString
                },
                {
                    Log.d("MainActivity", "Error")
                    text.text = it.message
                }
            ))
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
