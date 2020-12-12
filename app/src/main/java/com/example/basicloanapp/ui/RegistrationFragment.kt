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
import androidx.navigation.compose.navArgument
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.basicloanapp.LoanApplication

import com.example.basicloanapp.R
import com.example.basicloanapp.di.ViewModelFactory
import com.example.basicloanapp.util.Constants
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import javax.inject.Inject

class RegistrationFragment : BaseFragment() {
    private lateinit var model: RegistrationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        retainInstanceState()

        view.button_register.setOnClickListener {
            model.register(registration_name_input.editText?.text.toString().trim(),
                        registration_password_input.editText?.text.toString().trim(),
                        registration_repeat_password_input.editText?.text.toString().trim())
        }

        model.validationResult.observe(viewLifecycleOwner, Observer {
            handleValidation(it)
        })

        view.redirect_to_login.setOnClickListener {
            navigateToLogin()
        }
        return view
    }



    // Todo: Solve problem with localization
    private fun handleValidation(state: RegisterValidation) {
        clearErrors()
        when(state) {
            RegisterValidation.ACCOUNT_ALREADY_EXISTS -> {
                registration_error.visibility = View.VISIBLE
                registration_error.text = state.message
            }
            RegisterValidation.AWAITING -> {
                registration_progress_bar.visibility = View.VISIBLE
            }
            RegisterValidation.NOT_EQUAL_PASSWORDS -> {
                registration_password_input.error = state.message
            }
            RegisterValidation.NETWORK -> {
                registration_error.visibility = View.VISIBLE
                registration_error.text = state.message
            }
            RegisterValidation.NAME_EMPTY -> {
                registration_name_input.error = state.message
            }
            RegisterValidation.PASSWORD_EMPTY -> {
                registration_password_input.error = state.message
            }
            RegisterValidation.REPEAT_PASSWORD_EMPTY -> {
                registration_repeat_password_input.error = state.message
            }
            RegisterValidation.GOOD -> {
                navigateToList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.apply {
            model.saveState(
                registration_name_input.editText?.text.toString().trim(),
                registration_password_input.editText?.text.toString().trim(),
                registration_repeat_password_input.editText?.text.toString().trim()
            )
        }
    }

    private fun retainInstanceState() {
        model.name.observe(viewLifecycleOwner, Observer {
            view?.registration_name_input?.editText?.setText(it)
        })
        model.password.observe(viewLifecycleOwner, Observer {
            view?.registration_password_input?.editText?.setText(it)
        })
        model.repeatPassword.observe(viewLifecycleOwner, Observer {
            view?.registration_repeat_password_input?.editText?.setText(it)
        })
    }

    private fun clearErrors() {
        registration_name_input.error = null
        registration_password_input.error = null
        registration_repeat_password_input.error = null
        registration_error.visibility = View.GONE
        registration_progress_bar.visibility = View.INVISIBLE
    }

    private  fun navigateToList() {
        navController.navigate(R.id.action_registrationFragment_to_loanListFragment)
    }

    private fun navigateToLogin() {
        navController.navigate(R.id.action_registrationFragment_to_loginFragment)
    }
}
