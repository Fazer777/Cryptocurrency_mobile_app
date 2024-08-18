package com.project.cryptocurrencyapp.cryptocurrency.usecases

import com.project.domain.cryptocurrency.models.CryptocurrencyDomain
import com.project.domain.cryptocurrency.repository.ICryptocurrencyRepository

class GetCryptocurrenciesUseCase(
    private val cryptocurrencyRepository: ICryptocurrencyRepository
) {
    suspend fun execute(vsCurrency: String,) : Result<List<CryptocurrencyDomain>>{
        return cryptocurrencyRepository.getCryptocurrencies(vsCurrency = vsCurrency)
    }
}