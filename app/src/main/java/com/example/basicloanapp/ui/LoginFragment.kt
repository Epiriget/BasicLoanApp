package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.basicloanapp.LoanApplication

import com.example.basicloanapp.R
import com.example.basicloanapp.util.Constants
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var model: LoginViewModel
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as LoanApplication).repositoryComponent.inject(this)
        model = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.button_login.setOnClickListener { model.login(
            login_name_input.editText?.text.toString(),
            login_password_input.editText?.text.toString())
        }

        view.redirect_to_registration.setOnClickListener {
            navigateToRegistration()
        }

        model.validationResult.observe(viewLifecycleOwner, Observer {
            handleValidation(it)
        })

        return view
    }



    private fun handleValidation(state: LoginValidation?) {
        when(state) {
            LoginValidation.USER_NOT_FOUND -> {
                login_name_input.error = state.message
            }
            LoginValidation.NETWORK -> {
                login_error.visibility = View.VISIBLE
                login_error.text = state.message
            }
            LoginValidation.DEFAULT -> {
                login_name_input.error = null
                login_error.text = state.message
            }
            LoginValidation.AUTHORIZED -> {
                navigateToLoanList()
            }
        }
    }

    private fun navigateToRegistration() {
        navController.navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    private fun navigateToLoanList() {
        navController.navigate(R.id.action_loginFragment_to_loanListFragment)
    }
}
