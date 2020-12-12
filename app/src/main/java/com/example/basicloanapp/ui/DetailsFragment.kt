package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.basicloanapp.R
import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.service.LoanBodyResponse
import com.example.basicloanapp.util.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.view.*

class DetailsFragment : BaseFragment() {
    private lateinit var model: DetailsViewModel
    private lateinit var _view: View
    private var actionBar: ActionBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        val id = arguments?.getInt(Constants.DETAILS_ID_KEY)
        model.getLoan(id ?: 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.title = getString(R.string.details_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateToList()
        }.isEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navigateToList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
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
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.title = getString(R.string.app_name)
        navController.navigate(R.id.action_detailsFragment_to_loanListFragment)
    }

    private fun initFields(loan: Loan) {
        val res = _view.resources

        _view.details_amount.text = res.getString(R.string.details_amount, loan.amount)
        _view.details_name.text = res.getString(R.string.details_name, loan.firstName)
        _view.details_surname.text = res.getString(R.string.details_surname, loan.lastName)
        _view.details_percent.text = res.getString(R.string.details_percent, loan.percent)
        _view.details_phone.text = res.getString(R.string.details_phone, loan.phoneNumber)
        _view.details_state.text = res.getString(R.string.details_state, loan.state)
        _view.details_date.text = res.getString(R.string.details_date, loan.date)
        _view.details_period.text = res.getString(R.string.details_period, loan.period)

        val repayAmount: Int = (loan.amount.toDouble() * (100.0 + loan.percent) / 100).toInt()
        _view.details_amount_repay.text = res.getString(R.string.details_amount, repayAmount)
        _view.details_date_repay.text = res.getString(R.string.details_date, loan.toDate)

        _view.details_layout.visibility = View.VISIBLE
        _view.details_progress_bar.visibility = View.GONE
    }

}
