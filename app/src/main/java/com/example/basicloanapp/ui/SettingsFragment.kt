package com.example.basicloanapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.basicloanapp.R
import com.example.basicloanapp.util.Constants
import com.yariksoffice.lingver.Lingver
import java.util.*

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var navController: NavController
    private var actionBar: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PreferenceManager.getDefaultSharedPreferences(requireActivity())
            .registerOnSharedPreferenceChangeListener(this)
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        actionBar = (requireActivity() as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.settings_name)
        }
        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateToList()
        }.isEnabled = true

        initLocalization()
        return view
    }

    private fun navigateToList() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.title = getString(R.string.app_name)
        navController.navigate(R.id.action_settingsFragment_to_loanListFragment)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
        android.R.id.home -> {
            navigateToList()
        }
    }
    return super.onOptionsItemSelected(item)
}

    private fun initLocalization() {
        val preference = findPreference<ListPreference>("setting_language")
        when(Lingver.getInstance().getLanguage()) {
            "en" -> preference?.setValueIndex(0)
            "ru" -> preference?.setValueIndex(1)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_main, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val locale = sharedPreferences?.getString(key, "en")
        requireContext().getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(Constants.LOCALE_PREFERENCE_KEY, locale)
            .apply()
        Lingver.getInstance().setLocale(requireContext(), Locale(locale?: "en"))
        restart()
    }

    private fun restart() {
        actionBar?.title = getString(R.string.settings_name)
        navController.navigate(R.id.action_settingsFragment_to_settingFragment)
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(requireActivity())
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}