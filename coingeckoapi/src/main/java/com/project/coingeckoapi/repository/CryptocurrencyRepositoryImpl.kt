package com.project.coingeckoapi.repository

import com.project.coingeckoapi.api.CoinGeckoApi
import com.project.coingeckoapi.models.Cryptocurrency
import com.project.domain.cryptocurrency.models.CryptocurrencyDomain
import com.project.domain.cryptocurrency.repository.ICryptocurrencyRepository
import kotlin.Result

class CryptocurrencyRepositoryImpl(
    private val coinGeckoApi: CoinGeckoApi,
) : ICryptocurrencyRepository {

    override suspend fun getCryptocurrencies(vsCurrency: String): Result<List<CryptocurrencyDomain>> {

        val result = coinGeckoApi.coinsMarkets(vsCurrency = vsCurrency, perPage = 20)

        result.fold(
            onSuccess = {
                return Result.success(mapToDomain(it))
            },
            onFailure = { exception ->
                return Result.failure(exception)
            }
        )
    }


    private fun mapToDomain(cryptocurrencies: List<Cryptocurrency>): List<CryptocurrencyDomain> {
        return cryptocurrencies.map {
            CryptocurrencyDomain(
                id = it.id,
                symbol = it.symbol,
                name = it.name,
                image = it.image,
                currentPrice = it.currentPrice,
                priceChangePercentage = it.priceChangePercentage
            )
        }
    }

}

