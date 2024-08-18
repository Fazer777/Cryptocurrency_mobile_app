package com.project.domain.cryptocurrency.repository

import com.project.domain.cryptocurrency.models.CryptocurrencyDomain
import java.util.Currency

interface ICryptocurrencyRepository {
    suspend fun getCryptocurrencies(vsCurrency: String) : Result<List<CryptocurrencyDomain>>
}