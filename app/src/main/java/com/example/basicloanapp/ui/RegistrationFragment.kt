package com.example.basicloanapp.ui

import android.app.Activity
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
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.basicloanapp.LoanApplication

import com.example.basicloanapp.R
import com.example.basicloanapp.util.Constants
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import javax.inject.Inject

class RegistrationFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var model: RegistrationViewModel
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as LoanApplication).repositoryComponent.inject(this)
        // Todo: check whether it creates different instances of viewmodel
        model = ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        view.button_register.setOnClickListener {
            model.register(registration_name_input.editText?.text.toString(),
                        registration_password_input.editText?.text.toString(),
                        registration_repeat_password_input.editText?.text.toString())
        }

        model.validationResult.observe(viewLifecycleOwner, Observer {
            handleValidation(it)
        })

        view.redirect_to_login.setOnClickListener {
            navigateToLogin(
                registration_name_input.editText?.text.toString(),
                registration_password_input.editText?.text.toString()
            )
        }
        return view
    }

    private fun handleValidation(state: RegisterValidation) {
        when(state) {
            RegisterValidation.ACCOUNT_ALREADY_EXISTS -> {
                registration_name_input.error = state.message
            }
            RegisterValidation.NOT_EQUAL_PASSWORDS -> {
                registration_password_input.error = state.message
            }
            RegisterValidation.NETWORK -> {
                registration_error.visibility = View.VISIBLE
                registration_error.text = state.message
            }
            RegisterValidation.DEFAULT -> {
                registration_name_input.error = null
                registration_password_input.error = null
                registration_error.visibility = View.GONE
            }
        }
    }

    private fun navigateToLogin(name: String?, password: String?) {
        val bundle = Bundle().apply {
            putString(Constants.NAME_KEY, name)
            putString(Constants.PASSWORD_KEY, password)
        }
        navController.navigate(R.id.action_registrationFragment_to_loginFragment, bundle)
    }
}
