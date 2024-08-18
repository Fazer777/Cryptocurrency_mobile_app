package com.project.cryptocurrencyapp.cryptocurrency.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.cryptocurrencyapp.cryptocurrency.usecases.GetCryptocurrenciesUseCase
import com.project.domain.cryptocurrency.models.CryptocurrencyDomain
import kotlinx.coroutines.launch

class CryptocurrenciesViewModel(
    private val getCryptocurrenciesUseCase: GetCryptocurrenciesUseCase,
) : ViewModel() {

    //region Cryptocurrencies

    private val _cryptocurrencyState = MutableLiveData<CryptocurrencyUiState>()
    val cryptocurrencyState: LiveData<CryptocurrencyUiState> = _cryptocurrencyState

    sealed class CryptocurrencyUiState() {
        object Loading : CryptocurrencyUiState()
        class Success(val data: List<CryptocurrencyDomain>?) : CryptocurrencyUiState()
        class Error(val error: String) : CryptocurrencyUiState()
    }

    fun getCryptocurrencies(vsCurrency: String) {
        _cryptocurrencyState.value = CryptocurrencyUiState.Loading
        viewModelScope.launch {
            runCatching {
                getCryptocurrenciesUseCase.execute(vsCurrency = vsCurrency)
            }.onSuccess { result ->
                result.fold(
                    onSuccess = {list->
                        _cryptocurrencyState.value = CryptocurrencyUiState.Success(list)
                    },
                    onFailure = {exception ->
                        _cryptocurrencyState.value = CryptocurrencyUiState.Error(exception.message ?: "Something goes wrong!")
                    }
                )
            }.onFailure { error ->
                _cryptocurrencyState.value =
                    CryptocurrencyUiState.Error(error.message ?: "Something goes wrong!!2")
            }
        }
    }

    //endregion Cryptocurrencies

}