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
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var model: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        (requireActivity().application as LoanApplication).repositoryComponent.inject(this)
        model = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        view.button_login.setOnClickListener { model.login(
            login_name_input.editText?.text.toString(),
            login_password_input.editText?.text.toString())
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
        }
    }

}
