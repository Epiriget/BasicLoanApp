package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.basicloanapp.LoanApplication

import com.example.basicloanapp.R
import com.example.basicloanapp.data.LoanRepository
import com.example.basicloanapp.di.ViewModelFactory
import dagger.Lazy
import javax.inject.Inject

class SplashFragment : BaseFragment() {
    private lateinit var model: SplashViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        model.isAuthenticated.observe(viewLifecycleOwner, Observer {
            if(it) {
                navigateToList()
            } else {
                navigateToRegistration()
            }
        })
        model.authenticate()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun navigateToRegistration() {
        navController.navigate(R.id.action_splashFragment_to_registrationFragment)
    }

    private fun navigateToList() {
        navController.navigate(R.id.action_splashFragment_to_loanListFragment)
    }

}
