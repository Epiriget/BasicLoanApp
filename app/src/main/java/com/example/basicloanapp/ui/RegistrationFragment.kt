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

        model.notValidDescription.observe(viewLifecycleOwner, Observer {
            handleValidation(it)
        })
        return view
    }

    private fun handleValidation(state: ValidationResponse) {
        when(state) {
            ValidationResponse.ACCOUNT_ALREADY_EXISTS -> {
                registration_name_input.error = state.message
            }
            ValidationResponse.NOT_EQUAL_PASSWORDS -> {
                registration_password_input.error = state.message
            }
            ValidationResponse.NETWORK -> {
                registration_error.visibility = View.VISIBLE
                registration_error.error = state.message
            }
            ValidationResponse.DEFAULT -> {
                registration_name_input.error = null
                registration_password_input.error = null
            }
        }
    }

}
