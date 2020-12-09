package com.example.basicloanapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.basicloanapp.LoanApplication
import com.example.basicloanapp.R
import com.example.basicloanapp.di.ViewModelFactory
import dagger.Lazy
import javax.inject.Inject


abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as LoanApplication).repositoryComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        return null
    }
}
