package com.project.cryptocurrencyapp.cryptocurrency.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.project.cryptocurrencyapp.R
import com.project.cryptocurrencyapp.databinding.ItemCryptocurrencyViewBinding
import com.project.domain.cryptocurrency.models.CryptocurrencyDomain

class CryptocurrencyAdapter(
    private val context : Context
) :
    RecyclerView.Adapter<CryptocurrencyAdapter.CryptocurrencyViewHolder>() {

    // TODO("ADD DIFF UTILS")

    private val cryptocurrencies = mutableListOf<CryptocurrencyDomain>()

    private var typeViewPrice = TypeViewPrice.USD

    internal enum class TypeViewPrice(val type: String) {
        USD("usd"),
        RUB("rub");

        companion object {
            fun fromString(type: String): TypeViewPrice? {
                return entries.find { it.type.equals(type, ignoreCase = true) }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptocurrencyViewHolder {

        return CryptocurrencyViewHolder(
            ItemCryptocurrencyViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return cryptocurrencies.size
    }

    override fun onBindViewHolder(holder: CryptocurrencyViewHolder, position: Int) {
        holder.bind(cryptocurrencies[position])
    }

    inner class CryptocurrencyViewHolder(
        private val binding: ItemCryptocurrencyViewBinding,
    ) : ViewHolder(binding.root) {

        fun bind(cryptocurrencyDomain: CryptocurrencyDomain) = with(binding) {
            ivImageCryptocurrency.load(cryptocurrencyDomain.image) { crossfade(true) }
            tvNameCryptocurrency.text = cryptocurrencyDomain.name
            tvSymbolCryptocurrency.text = cryptocurrencyDomain.symbol
            setTypePrice(cryptocurrencyDomain.currentPrice)
            setTypePriceChange(cryptocurrencyDomain.priceChangePercentage)

        }

        private fun setTypePrice(currentPrice: Double) = with(binding) {
            when (typeViewPrice) {
                TypeViewPrice.USD -> tvCurrPriceCryptocurrency.text =
                    String.format("\u0024 %,.2f", currentPrice)

                TypeViewPrice.RUB -> tvCurrPriceCryptocurrency.text =
                    String.format("\u20BD %,.2f", currentPrice)
            }
        }

        private fun setTypePriceChange(priceChangePercentage: Float) = with(binding) {
            if (priceChangePercentage > 0) {
                tvPriceChangePercentage.text = String.format("+%.2f%%", priceChangePercentage)
                tvPriceChangePercentage.setTextColor(context.resources.getColor(R.color.dark_cyan, null))
            } else {
                tvPriceChangePercentage.text = String.format("%.2f%%", priceChangePercentage)
                tvPriceChangePercentage.setTextColor(context.resources.getColor(R.color.red, null))
            }
        }
    }

    private fun setTypeViewPrice(typePrice: String) {
        typeViewPrice = TypeViewPrice.valueOf(typePrice)
    }

    // TODO("NEED Changes to Diff utils")
    fun setData(newData: List<CryptocurrencyDomain>) {
        cryptocurrencies.clear()
        cryptocurrencies.addAll(newData)
        notifyDataSetChanged()
    }
}