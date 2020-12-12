package com.example.basicloanapp.ui.loan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.basicloanapp.R
import com.example.basicloanapp.service.LoanConditions
import com.example.basicloanapp.ui.BaseFragment
import com.example.basicloanapp.ui.MainActivity
import com.example.basicloanapp.ui.validation.CreateValidation
import kotlinx.android.synthetic.main.fragment_create_loan.view.*

class CreateLoanFragment : BaseFragment() {
    private lateinit var model: CreateLoanViewModel
    private lateinit var conditions: LoanConditions
    private lateinit var _view: View
    private var actionBar: ActionBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[CreateLoanViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.title = getString(R.string.create_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateToList()
        }.isEnabled = true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _view = inflater.inflate(R.layout.fragment_create_loan, container, false)
        retainInstanceState()
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

        _view.create_amount_input.helperText = _view.resources.getString(R.string.conditions_amount, conditions.maxAmount)
        _view.conditions_percent.text = _view.resources.getString(R.string.conditions_percent, conditions.percent)
        _view.conditions_period.text = _view.resources.getString(R.string.conditions_period, conditions.period)
    }

    private fun handleValidation(state: CreateValidation) {
        clearErrorsFields()
        when(state) {
            CreateValidation.AMOUNT_TYPE_ERROR -> {
                _view.create_amount_input.error = getString(state.message)
            }
            CreateValidation.AMOUNT_SIZE_ERROR -> {
                _view.create_amount_input.error = getString(state.message).format(conditions.maxAmount)
            }
            CreateValidation.NAME_EMPTY -> {
                _view.create_name_input.error = getString(state.message)
            }
            CreateValidation.SURNAME_EMPTY -> {
                _view.create_surname_input.error = getString(state.message)
            }
            CreateValidation.PHONE_EMPTY -> {
                _view.create_phone_input.error = getString(state.message)
            }
            CreateValidation.NETWORK -> {
                _view.create_error.visibility = View.VISIBLE
                _view.create_error.text = getString(state.message)
            }
            CreateValidation.GOOD -> {
                navigateToList()
            }
        }
    }

    private fun clearErrorsFields() {
        _view.create_amount_input.error = null
        _view.create_name_input.error = null
        _view.create_surname_input.error = null
        _view.create_phone_input.error = null
        _view.create_error.visibility = View.INVISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.apply {
            model.saveState(
                create_amount_input.editText?.text.toString(),
                create_name_input.editText?.text.toString().trim(),
                create_surname_input.editText?.text.toString().trim(),
                create_phone_input.editText?.text.toString().trim()
            )
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navigateToList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun retainInstanceState() {
        model.name.observe(viewLifecycleOwner, Observer {
            view?.create_name_input?.editText?.setText(it)
        })
        model.surname.observe(viewLifecycleOwner, Observer {
            view?.create_surname_input?.editText?.setText(it)
        })
        model.amount.observe(viewLifecycleOwner, Observer {
            view?.create_amount_input?.editText?.setText(it)
        })
        model.phone.observe(viewLifecycleOwner, Observer {
            view?.create_phone_input?.editText?.setText(it)
        })
    }

    private fun navigateToList() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.title = getString(R.string.app_name)
        navController.navigate(R.id.action_createLoanFragment_to_loanListFragment)
    }
}
