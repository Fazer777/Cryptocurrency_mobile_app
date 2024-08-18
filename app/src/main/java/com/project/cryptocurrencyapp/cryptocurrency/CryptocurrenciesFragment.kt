package com.project.cryptocurrencyapp.cryptocurrency

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.cryptocurrencyapp.R
import com.project.cryptocurrencyapp.cryptocurrency.adapters.CryptocurrencyAdapter
import com.project.cryptocurrencyapp.cryptocurrency.viewmodels.CryptocurrenciesViewModel
import com.project.cryptocurrencyapp.databinding.FragmentCryptocurrenciesBinding
import com.project.cryptocurrencyapp.utils.network.IConnectivityObserver
import com.project.cryptocurrencyapp.utils.network.NetworkConnectivityObserver
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log10


class CryptocurrenciesFragment : Fragment() {

    private var _binding: FragmentCryptocurrenciesBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val cryptocurrenciesViewModel: CryptocurrenciesViewModel by viewModel()

    private lateinit var cryptocurrencyAdapter: CryptocurrencyAdapter

    private lateinit var connectivityObserver: NetworkConnectivityObserver

    private val TAG = "AAA"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCryptocurrenciesBinding.inflate(
            LayoutInflater.from(container?.context),
            container,
            false
        )

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.cryptocurrency_list)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        connectivityObserver = NetworkConnectivityObserver(requireContext())
        initRecyclerView()
        initObservers()
        initListeners()

        val state = cryptocurrenciesViewModel.cryptocurrencyState.value
        if (state is CryptocurrenciesViewModel.CryptocurrencyUiState.Success){
            cryptocurrencyAdapter.setData(state.data!!)
        }
        else{
            checkInitialNetworkStatus()
        }
    }

    private fun checkInitialNetworkStatus() {
        Log.d(TAG, "checkInitialNetworkStatus")
        val initialStatus = connectivityObserver.getCurrentStatus()
        when (initialStatus) {
            IConnectivityObserver.Status.Available -> {
                getCryptocurrencyListUSD()
            }

            IConnectivityObserver.Status.Unavailable -> {
                hideLoading()
                showError()
                Toast.makeText(
                    requireContext(),
                    "Internet connection is unavailable",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

    private fun initObservers() = with(cryptocurrenciesViewModel) {
        cryptocurrencyState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CryptocurrenciesViewModel.CryptocurrencyUiState.Loading -> {
                    hideError()
                    showLoading()
                    Log.d(TAG, "initObservers: Loading")
                }

                is CryptocurrenciesViewModel.CryptocurrencyUiState.Success -> {
                    hideError()
                    hideLoading()
                    cryptocurrencyAdapter.setData(newData = state.data!!)
                    Log.d(TAG, "initObservers: Success")
                }

                is CryptocurrenciesViewModel.CryptocurrencyUiState.Error -> {
                    hideLoading()
                    showError()
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "initObservers: Error")
                }
            }
        }

        connectivityObserver.observe().onEach { status ->
            when (status) {
                IConnectivityObserver.Status.Available -> {
                    Log.d(TAG, "initObservers: Available")
                }

                IConnectivityObserver.Status.Unavailable -> {
                    hideLoading()
                    showError()
                    Log.d(TAG, "initObservers: Unavailable")
                }

                IConnectivityObserver.Status.Losing -> {
                    hideLoading()
                    showError()
                    Log.d(TAG, "initObservers: Losing")
                }

                IConnectivityObserver.Status.Lost -> {
                    hideLoading()
                    showError()
                    Log.d(TAG, "initObservers: Lost")
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun showError() = with(binding) {
        errorScreen.root.visibility = View.VISIBLE
        rvCryptocurrency.visibility = View.GONE
    }

    private fun hideError() = with(binding) {
        errorScreen.root.visibility = View.GONE
    }

    private fun showLoading() = with(binding) {
        loadingScreen.root.visibility = View.VISIBLE
        rvCryptocurrency.visibility = View.GONE
    }

    private fun hideLoading() = with(binding) {
        loadingScreen.root.visibility = View.GONE
        rvCryptocurrency.visibility = View.VISIBLE
    }

    private fun initRecyclerView() = with(binding) {
        cryptocurrencyAdapter = CryptocurrencyAdapter(requireContext())
        rvCryptocurrency.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvCryptocurrency.adapter = cryptocurrencyAdapter
    }

    private fun initListeners() = with(binding) {
        errorScreen.btnTryAgain.setOnClickListener {
            checkInitialNetworkStatus()
        }

        chipRub.setOnClickListener {
            getCryptocurrencyListRUB()
        }

        chipUsd.setOnClickListener {
            getCryptocurrencyListUSD()
        }
    }

    private fun getCryptocurrencyListUSD() {
        cryptocurrencyAdapter.setTypeViewPrice("usd")
        cryptocurrenciesViewModel.getCryptocurrencies("usd")
        switchCurrency(isUsd = true)
    }

    private fun getCryptocurrencyListRUB() {
        cryptocurrencyAdapter.setTypeViewPrice("rub")
        cryptocurrenciesViewModel.getCryptocurrencies("rub")
        switchCurrency(isUsd = false)
    }

    private fun switchCurrency(isUsd: Boolean) = with(binding) {
        chipUsd.isChecked = isUsd
        chipRub.isChecked = !isUsd
    }

    private fun initToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_cryptocurrency_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}