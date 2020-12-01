package com.example.basicloanapp.di

import com.example.basicloanapp.service.LoanService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class LoanServiceModule {
    @Provides
    @Singleton
    fun loanService(): LoanService {
        val interceptor  = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder().build()
                    val response = chain.proceed(request)
                    return response
                }
            })
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://focusapp-env.eba-xm2atk2z.eu-central-1.elasticbeanstalk.com/")
            .client(client)
            .addConverterFactory(provideGSON())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(provideRXCallAdapter())
            .build()
            .create(LoanService::class.java)
    }

    @Provides
    @Singleton
    fun provideGSON(): GsonConverterFactory = GsonConverterFactory.create(
        GsonBuilder()
        .setLenient()
        .create()
    )

    @Provides
    @Singleton
    fun provideRXCallAdapter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
}