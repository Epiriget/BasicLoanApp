package com.example.basicloanapp.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule() {
    @Singleton
    @Provides
    fun application(application: Application): Application {
        return application
    }
}