package com.example.basicloanapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.basicloanapp.LoanApplication

import com.example.basicloanapp.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import javax.inject.Inject

class RegistrationFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var model: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        (requireActivity().application as LoanApplication).repositoryComponent.inject(this)

        // Todo: check whether it creates different instances of viewmodel
        model = ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]

        view.button_register.setOnClickListener {
            model.register(registration_name_input.editText?.text.toString(),
                        registration_password_input.editText?.text.toString(),
                        registration_repeat_password_input.editText?.text.toString())
        }

        model.validationResult.observe(viewLifecycleOwner, Observer {
            handleValidation(it)
        })
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
                registration_error.visibility = View.INVISIBLE
            }
        }
    }

}
