package com.project.coingeckoapi.models

import com.google.gson.annotations.SerializedName

data class CryptocurrencyDetails(
    @SerializedName("id")
    val id : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("categories")
    val categories : List<String>,
    @SerializedName("description")
    val description: Description,
    @SerializedName("image")
    val image: Image,
)

data class Description(
    @SerializedName("en")
    val en : String,
)

data class Image (
    @SerializedName("small")
    val small : String,
)