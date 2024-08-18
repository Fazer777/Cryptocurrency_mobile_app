package com.project.domain.cryptocurrency.models

data class CryptocurrencyDomain(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage: Float,
)