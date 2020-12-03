package com.example.basicloanapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.basicloanapp.LoanApplication

import com.example.basicloanapp.R
import com.example.basicloanapp.di.ViewModelFactory
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

        model = ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]

        view.button_register.setOnClickListener {
            model.login(registration_name_input.editText.toString(),
                        registration_password_input.editText.toString(),
                        registration_repeat_password_input.editText.toString())
        }
        return view
    }

}
