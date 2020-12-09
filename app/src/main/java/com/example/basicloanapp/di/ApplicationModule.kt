package com.example.basicloanapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.SavedStateHandle
import com.example.basicloanapp.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule {
    @Singleton
    @Provides
    fun sharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE)
    }
    @Singleton
    @Provides
    fun savedStateHandle(): SavedStateHandle {
        return SavedStateHandle()
    }
}