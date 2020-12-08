package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.basicloanapp.R
import com.example.basicloanapp.service.LoanBodyResponse
import com.example.basicloanapp.util.Constants
import kotlinx.android.synthetic.main.fragment_details.view.*
import okhttp3.internal.notifyAll

class DetailsFragment : BaseFragment() {
    private lateinit var model: DetailsViewModel
    private lateinit var _view: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        val id = arguments?.getInt(Constants.DETAILS_ID_KEY)
        model.getLoan(id ?: 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_details, container, false)

        model.loan.observe(viewLifecycleOwner, Observer {
            initFields(it)
        })

        _view.details_back.setOnClickListener {
            navigateToList()
        }

        return _view
    }

    private fun navigateToList() {
        navController.navigate(R.id.action_detailsFragment_to_loanListFragment)
    }

    private fun initFields(loan: LoanBodyResponse) {
        val res = _view.resources

        _view.details_amount.text = res.getString(R.string.details_amount, loan.amount)
        _view.details_name.text = res.getString(R.string.details_name, loan.firstName)
        _view.details_surname.text = res.getString(R.string.details_surname, loan.lastName)
        _view.details_percent.text = res.getString(R.string.details_percent, loan.percent)
        _view.details_phone.text = res.getString(R.string.details_phone, loan.phoneNumber)
        _view.details_state.text = res.getString(R.string.details_state, loan.state)
        // Todo: change date format
        _view.details_date.text = res.getString(R.string.details_date, loan.date)
        _view.details_period.text = res.getString(R.string.details_period, loan.period.toString())
    }

}
