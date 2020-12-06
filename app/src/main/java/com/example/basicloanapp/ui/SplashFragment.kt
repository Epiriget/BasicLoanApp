package com.example.basicloanapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.basicloanapp.R

class SplashFragment : BaseFragment() {
    private lateinit var model: SplashViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
        model.isAuthenticated.observe(viewLifecycleOwner, Observer {
            if(it) {
                navigateToList()
            } else {
                navigateToRegistration()
            }
        })
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    fun navigateToRegistration() {
        navController.navigate(R.id.action_splashFragment_to_registrationFragment)
    }

    fun navigateToList() {
        navController.navigate(R.id.action_splashFragment_to_loanListFragment)
    }

}
