package com.project.cryptocurrencyapp.di

import com.project.cryptocurrencyapp.cryptocurrency.usecases.GetCryptocurrenciesUseCase
import com.project.cryptocurrencyapp.cryptocurrency.viewmodels.CryptocurrenciesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<CryptocurrenciesViewModel> {
        CryptocurrenciesViewModel(getCryptocurrenciesUseCase = get())
    }

    factory<GetCryptocurrenciesUseCase> {
        GetCryptocurrenciesUseCase(cryptocurrencyRepository = get())
    }
}