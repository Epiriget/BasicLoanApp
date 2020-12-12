package com.example.basicloanapp.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.basicloanapp.R
import com.example.basicloanapp.ui.BaseFragment
import com.example.basicloanapp.ui.validation.LoginValidation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : BaseFragment() {
    private lateinit var model: LoginViewModel
    private lateinit var _view: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _view = inflater.inflate(R.layout.fragment_login, container, false)
        retainInstanceState()

        _view.button_login.setOnClickListener { model.login(
            login_name_input.editText?.text.toString(),
            login_password_input.editText?.text.toString())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = true

        _view.redirect_to_registration.setOnClickListener {
            navigateToRegistration()
        }

        model.validationResult.observe(viewLifecycleOwner, Observer {
            handleValidation(it)
        })

        return _view
    }



    private fun handleValidation(state: LoginValidation?) {
        clearErrorFields()
        when(state) {
            LoginValidation.AWAITING -> {
                login_progress_bar.visibility = View.VISIBLE
            }
            LoginValidation.USER_NOT_FOUND -> {
                login_name_input.error = getString(state.message)
            }
            LoginValidation.NETWORK -> {
                login_error.visibility = View.VISIBLE
                login_error.text = getString(state.message)
            }
            LoginValidation.NAME_EMPTY -> {
                login_name_input.error = getString(state.message)
            }
            LoginValidation.PASSWORD_EMPTY -> {
                login_password_input.error = getString(state.message)
            }
            LoginValidation.GOOD -> {
                navigateToLoanList()
            }
        }
    }

    // Todo: Is it ok to save state here?
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.apply {
            model.saveState(
                login_name_input.editText?.text.toString().trim(),
                login_password_input.editText?.text.toString().trim()
            )
        }
    }

    private fun retainInstanceState() {
        model.name.observe(viewLifecycleOwner, Observer {
            view?.login_name_input?.editText?.setText(it)
        })
        model.password.observe(viewLifecycleOwner, Observer {
            view?.login_password_input?.editText?.setText(it)
        })
    }


    private fun clearErrorFields() {
        _view.login_name_input.error = null
        login_password_input.error = null
        login_error.visibility = View.GONE
        login_progress_bar.visibility = View.GONE
    }

    private fun navigateToRegistration() {
        navController.navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    private fun navigateToLoanList() {
        navController.navigate(R.id.action_loginFragment_to_loanListFragment)
    }
}
