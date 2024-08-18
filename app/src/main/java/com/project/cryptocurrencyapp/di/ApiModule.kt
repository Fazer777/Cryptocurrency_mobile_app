package com.project.cryptocurrencyapp.di

import com.project.coingeckoapi.api.CoinGeckoApi
import com.project.coingeckoapi.api.getCoinGeckoApi
import com.project.coingeckoapi.repository.CryptocurrencyRepositoryImpl
import com.project.domain.cryptocurrency.repository.ICryptocurrencyRepository
import org.koin.dsl.module

val apiModule = module {

    single<ICryptocurrencyRepository> {
        CryptocurrencyRepositoryImpl(coinGeckoApi = get())
    }


    factory<CoinGeckoApi> {
        getCoinGeckoApi("http://api.coingecko.com/api/v3/")
    }
}