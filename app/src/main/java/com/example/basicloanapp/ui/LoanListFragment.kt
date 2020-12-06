package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basicloanapp.LoanApplication

import com.example.basicloanapp.R
import com.example.basicloanapp.service.LoanBodyResponse
import kotlinx.android.synthetic.main.fragment_loan_list.*
import javax.inject.Inject

class LoanListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var model: LoanListViewModel
    private lateinit var navController: NavController
    private lateinit var loanList: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as LoanApplication).repositoryComponent.inject(this)
        model = ViewModelProvider(this, viewModelFactory)[LoanListViewModel::class.java]
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loan_list, container, false)
        loanList = view.findViewById(R.id.list)
        val adapter = LoanAdapter { loan -> adapterOnClick(loan) }

        initRecyclerView(adapter)

        model.loans.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it as MutableList<LoanBodyResponse>)
        })
        return view
    }

    private fun adapterOnClick(loan: LoanBodyResponse) {
        //Todo:"Replace with details of loan"
        Toast.makeText(requireActivity(), "List Item clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView(adapter: LoanAdapter) {
        val recyclerLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        loanList.layoutManager = recyclerLayoutManager
        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        loanList.addItemDecoration(decoration)

        loanList.adapter = adapter
    }

}