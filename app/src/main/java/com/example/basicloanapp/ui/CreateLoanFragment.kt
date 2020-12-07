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
import com.example.basicloanapp.service.LoanCreateRequest
import kotlinx.android.synthetic.main.fragment_create_loan.view.*

class CreateLoanFragment : BaseFragment() {
    private lateinit var model: CreateLoanViewModel
    private lateinit var conditions: LoanConditions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[CreateLoanViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_loan, container, false)
        model.conditions.observe(viewLifecycleOwner, Observer {
            conditions = it
            setConditions(view, it)
        })

        view.create_button.setOnClickListener {
            //Todo: replace with validateLoan()
            model.createLoan(
                LoanCreateRequest(
                    view.create_amount_input.editText?.text.toString().toInt(),
                    view.create_name_input.editText?.text.toString(),
                    view.create_surname_input.editText?.text.toString(),
                    conditions.percent,
                    conditions.period,
                    view.create_phone_input.editText?.text.toString()
                )
            )
            navigateToList()
        }
        return view
    }

    private fun setConditions(view: View, conditions: LoanConditions) {
        view.conditions_amount.text = view.resources.getString(R.string.conditions_amount, conditions.maxAmount)
        view.conditions_percent.text = view.resources.getString(R.string.conditions_percent, conditions.percent)
        view.conditions_period.text = view.resources.getString(R.string.conditions_period, conditions.period)
    }

    private fun navigateToList() {
        navController.navigate(R.id.action_createLoanFragment_to_loanListFragment)
    }
}
