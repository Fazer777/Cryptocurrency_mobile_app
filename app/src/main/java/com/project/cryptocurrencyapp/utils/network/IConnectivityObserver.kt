package com.project.cryptocurrencyapp.utils.network

import kotlinx.coroutines.flow.Flow

interface IConnectivityObserver {
    fun observe() : Flow<Status>

    fun getCurrentStatus() : Status

    enum class Status{
        Available, Unavailable, Losing, Lost
    }
}