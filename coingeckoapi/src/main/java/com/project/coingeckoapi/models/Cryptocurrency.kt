package com.project.coingeckoapi.models

import com.google.gson.annotations.SerializedName

data class Cryptocurrency(
    @SerializedName("id")
    val id : String,
    @SerializedName("symbol")
    val symbol : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("image")
    val image : String,
    @SerializedName("current_price")
    val currentPrice : Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage: Float,
)

