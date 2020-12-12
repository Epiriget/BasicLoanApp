package com.example.basicloanapp

import android.app.Application
import android.content.Context
import androidx.compose.ui.unit.Constraints
import com.example.basicloanapp.di.*
import com.example.basicloanapp.util.Constants
import com.yariksoffice.lingver.Lingver
import java.util.*

class LoanApplication: Application() {
    lateinit var repositoryComponent: GraphComponent
    override fun onCreate() {
        super.onCreate()
        repositoryComponent = DaggerGraphComponent.builder()
            .context(this)
            .build()
        initLocale()
    }

    private fun initLocale() {
        val locale = getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE)
            .getString(Constants.LOCALE_PREFERENCE_KEY, "")
        Lingver.init(this, if(locale.isNullOrEmpty()) Locale.getDefault() else Locale(locale))
    }
}