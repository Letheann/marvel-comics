package com.example.comics.di

import com.example.comics.di.factory.NetworkResponseAdapterFactory
import com.example.comics.domain.CharUseCase
import com.example.comics.domain.CharUseCaseImpl
import com.example.comics.repository.CharRepository
import com.example.comics.repository.CharRepositoryImpl
import com.example.comics.repository.api.Api
import com.example.comics.viewmodel.MainActivityViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DI {

    private val instance = module {
        single(QualifierMarvelRetrofit) { provideRetrofit() }
        factory(QualifierMarvelApi) { provideApiRetrofitHost(get(QualifierMarvelRetrofit)) }
        factory<CharRepository> {
            CharRepositoryImpl(get(QualifierMarvelApi))
        }
        factory<CharUseCase> { CharUseCaseImpl(repository = get()) }
        viewModel { MainActivityViewModel(useCase = get()) }
    }

    val modules = listOf(instance)

    private fun provideApiRetrofitHost(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    private fun provideRetrofit(
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com/v1/public/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()
}