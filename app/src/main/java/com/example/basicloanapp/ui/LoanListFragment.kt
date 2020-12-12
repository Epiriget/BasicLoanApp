package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.basicloanapp.R
import com.example.basicloanapp.domain.entity.Loan
import com.example.basicloanapp.util.Constants
import kotlinx.android.synthetic.main.fragment_loan_list.*
import kotlinx.android.synthetic.main.fragment_loan_list.view.*


class LoanListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var model: LoanListViewModel
    private lateinit var loanList: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(this, viewModelFactory)[LoanListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this){}.isEnabled = true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_loan_list, container, false)
        refreshLayout = view.refresh_layout.also {
            it.setOnRefreshListener(this)
            it.isRefreshing = true
        }
        model.getLoans()


        loanList = view.findViewById(R.id.list)
        val adapter = LoanAdapter { id -> adapterOnClick(id) }

        initRecyclerView(adapter)
        model.loans.observe(viewLifecycleOwner, Observer {
            validateList(it)
            adapter.submitList(it as MutableList<Loan>)
            refreshLayout.isRefreshing = false
        })

        view.fab.setOnClickListener { navigateToCreateLoan() }
        return view
    }

    private fun validateList(list: List<Loan>) {
        if(list.isEmpty()) {
            refreshLayout.visibility = View.GONE
            list_empty_placeholder.visibility = View.VISIBLE
        } else {
            refreshLayout.visibility = View.VISIBLE
            list_empty_placeholder.visibility = View.GONE
        }
    }

    private fun adapterOnClick(id: Int) {
        val bundle = Bundle().apply {
            putInt(Constants.DETAILS_ID_KEY, id)
        }
        navController.navigate(R.id.action_loanListFragment_to_detailsFragment, bundle)
    }

    private fun initRecyclerView(adapter: LoanAdapter) {
        val recyclerLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        loanList.layoutManager = recyclerLayoutManager
        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        loanList.addItemDecoration(decoration)

        loanList.adapter = adapter
    }

    private fun navigateToCreateLoan() {
        navController.navigate(R.id.action_loanListFragment_to_createLoanFragment)
    }

    private fun logOut() {
        model.logOut()
        navController.navigate(R.id.action_loanListFragment_to_loginFragment)
    }

    override fun onRefresh() {
        model.getLoans()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_refresh -> {
                refreshLayout.isRefreshing = true
                model.getLoans()
            }
            R.id.menu_logout -> {
                logOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
