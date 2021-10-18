package com.better.tmdbtest.ui.main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.better.tmdbtest.R
import com.better.tmdbtest.databinding.ItemNetworkingBinding
import com.better.tmdbtest.domain.entity.Item
import com.better.tmdbtest.domain.entity.Networking
import com.better.tmdbtest.ui.main.adapter.BaseViewHolder
import com.better.tmdbtest.ui.main.adapter.ItemFingerprint

class NetworkingFingerprint(private val tryAgain: PressTryAgain) :
    ItemFingerprint<ItemNetworkingBinding, Networking> {
    override fun isRelativeItem(item: Item) = item is Networking

    override fun getLayoutId() = R.layout.item_networking

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemNetworkingBinding, Networking> {
        val binding = ItemNetworkingBinding.inflate(layoutInflater, parent, false)
        return NetworkingViewHolder(binding, tryAgain)
    }
}

interface PressTryAgain {
    fun tryAgain()
}

class NetworkingViewHolder(binding: ItemNetworkingBinding, val tryAgain: PressTryAgain) :
    BaseViewHolder<ItemNetworkingBinding, Networking>(binding) {

    override fun onBind(item: Networking) {
        binding.apply {
            if (item.isItError) {
                button.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {

                    }
                }
                progressBar.visibility = View.INVISIBLE
                tvMessage.text = item.message
                button.setOnClickListener {
                 tryAgain.tryAgain()
                }

            } else {
                button.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                tvMessage.text = "Loading"
            }
        }
    }
}