package com.project.coingeckoapi.api

import com.project.coingeckoapi.models.Cryptocurrency
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface CoinGeckoApi {

    @Headers("Accept: application/json")
    @GET("coins/markets")
    suspend fun coinsMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("per_page") perPage : Int = 30
    ): Result<List<Cryptocurrency>>



}

fun getCoinGeckoApi(baseUrl: String): CoinGeckoApi =
    retrofit(baseUrl).create(CoinGeckoApi::class.java)

private fun retrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

