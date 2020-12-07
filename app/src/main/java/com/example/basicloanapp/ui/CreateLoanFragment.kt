package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.basicloanapp.R
import com.example.basicloanapp.service.LoanConditions
import kotlinx.android.synthetic.main.fragment_create_loan.view.*

class CreateLoanFragment : BaseFragment() {
    private lateinit var model: CreateLoanViewModel
    private lateinit var conditions: LoanConditions
    private lateinit var _view: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[CreateLoanViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_create_loan, container, false)
        model.conditions.observe(viewLifecycleOwner, Observer {
            conditions = it
            setConditionFields(it)
        })

        _view.create_button.setOnClickListener {
            model.createLoan(
                    _view.create_amount_input.editText?.text.toString(),
                    _view.create_name_input.editText?.text.toString().trim(),
                    _view.create_surname_input.editText?.text.toString().trim(),
                    conditions.percent,
                    conditions.period,
                    _view.create_phone_input.editText?.text.toString().trim()
            )
        }

        model.validationResult.observe(viewLifecycleOwner, Observer {
            handleValidation(it)
        })
        return _view
    }

    private fun setConditionFields(conditions: LoanConditions) {
        _view.conditions_amount.text = _view.resources.getString(R.string.conditions_amount, conditions.maxAmount)
        _view.conditions_percent.text = _view.resources.getString(R.string.conditions_percent, conditions.percent)
        _view.conditions_period.text = _view.resources.getString(R.string.conditions_period, conditions.period)
    }

    private fun handleValidation(state: CreateValidation) {
        clearValidation()
        when(state) {
            CreateValidation.AMOUNT_TYPE_ERROR -> {
                _view.create_amount_input.error = state.message
            }
            CreateValidation.AMOUNT_SIZE_ERROR -> {
                _view.create_amount_input.error = state.message
            }
            CreateValidation.NAME_EMPTY -> {
                _view.create_name_input.error = state.message
            }
            CreateValidation.SURNAME_EMPTY -> {
                _view.create_surname_input.error = state.message
            }
            CreateValidation.PHONE_EMPTY -> {
                _view.create_phone_input.error = state.message
            }
            CreateValidation.NETWORK -> {
                _view.create_error.visibility = View.VISIBLE
                _view.create_error.text = state.message
            }
            CreateValidation.GOOD -> {
                navigateToList()
            }
        }
    }

    private fun clearValidation() {
        _view.create_amount_input.error = null
        _view.create_name_input.error = null
        _view.create_surname_input.error = null
        _view.create_phone_input.error = null
        _view.create_error.visibility = View.INVISIBLE
    }

    private fun navigateToList() {
        navController.navigate(R.id.action_createLoanFragment_to_loanListFragment)
    }
}
