package com.project.cryptocurrencyapp.cryptocurrency

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.cryptocurrencyapp.R
import com.project.cryptocurrencyapp.cryptocurrency.adapters.CryptocurrencyAdapter
import com.project.cryptocurrencyapp.cryptocurrency.viewmodels.CryptocurrenciesViewModel
import com.project.cryptocurrencyapp.databinding.FragmentCryptocurrenciesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class CryptocurrenciesFragment : Fragment() {

    private var _binding: FragmentCryptocurrenciesBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val cryptocurrenciesViewModel: CryptocurrenciesViewModel by viewModel()

    private lateinit var cryptocurrencyAdapter: CryptocurrencyAdapter


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
        cryptocurrencyAdapter = CryptocurrencyAdapter(requireContext())
        initRecyclerView()
        initObservers()
        initListeners()
        getCryptocurrencyListUSD()
    }

    private fun initObservers() = with(cryptocurrenciesViewModel) {
        cryptocurrencyState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CryptocurrenciesViewModel.CryptocurrencyUiState.Loading -> {
                    hideError()
                    showLoading()
                }

                is CryptocurrenciesViewModel.CryptocurrencyUiState.Success -> {
                    hideError()
                    hideLoading()
                    cryptocurrencyAdapter.setData(newData = state.data!!)
                }

                is CryptocurrenciesViewModel.CryptocurrencyUiState.Error -> {
                    hideLoading()
                    showError()
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                    Log.d("AAA", state.error)
                }
            }
        }
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
        rvCryptocurrency.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvCryptocurrency.adapter = cryptocurrencyAdapter
    }

    private fun initListeners() = with(binding) {
        errorScreen.btnTryAgain.setOnClickListener {
            getCryptocurrencyListUSD()
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