package com.better.tmdbtest.ui.main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.better.tmdbtest.R
import com.better.tmdbtest.databinding.ItemFilmBinding
import com.better.tmdbtest.domain.entity.Film
import com.better.tmdbtest.domain.entity.Item
import com.better.tmdbtest.ui.main.adapter.BaseViewHolder
import com.better.tmdbtest.ui.main.adapter.ItemFingerprint
import com.bumptech.glide.Glide

class FilmFingerprint(private val addToFavorite: AddToFavorite) :
    ItemFingerprint<ItemFilmBinding, Film> {
    override fun isRelativeItem(item: Item) = item is Film

    override fun getLayoutId() = R.layout.item_film

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemFilmBinding, Film> {
        val binding = ItemFilmBinding.inflate(layoutInflater, parent, false)
        return FilmViewHolder(binding, addToFavorite)
    }
}

interface AddToFavorite {
    fun addToFavorite(film: Film, position: Int)
}

class FilmViewHolder(binding: ItemFilmBinding, private val addToFavorite: AddToFavorite) :
    BaseViewHolder<ItemFilmBinding, Film>(binding) {
    override fun onBind(item: Film) {
        binding.apply {
            Glide.with(this.root)
                .load(item.imageUrl)
                .error(R.drawable.ic_film_error)
                .into(ivFilmLogo)
            tvFilmTitle.text = item.title
            tvFilmDescription.text = item.description
            tvFilmVote.text = item.voteAverage.toString()
            tvFilmVoteCount.text = item.voteCount.toString()
            if (item.releaseDate.isEmpty()) tvFilmDateRelease.visibility = View.GONE
            else {
                tvFilmDateRelease.visibility = View.VISIBLE
                tvFilmDateRelease.text = item.releaseDate
            }

            if (item.isFavorite) {
                ivAddToFavorite.setImageResource(R.drawable.ic_added_to_favorite)
            } else ivAddToFavorite.setImageResource(R.drawable.ic_add_to_favorite)

            ivAddToFavorite.setOnClickListener {
                item.isFavorite = !item.isFavorite
                addToFavorite.addToFavorite(item, position)
            }
        }
    }
}