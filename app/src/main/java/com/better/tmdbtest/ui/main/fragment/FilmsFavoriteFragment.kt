package com.better.tmdbtest.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.better.tmdbtest.databinding.FragmentFilmsFavoriteBinding
import com.better.tmdbtest.domain.entity.Film
import com.better.tmdbtest.ui.main.MainViewModel
import com.better.tmdbtest.ui.main.adapter.FilmAdapter
import com.better.tmdbtest.ui.main.adapter.fingerprints.AddToFavorite
import com.better.tmdbtest.ui.main.adapter.fingerprints.FilmFingerprint
import com.better.tmdbtest.ui.main.adapter.fingerprints.NetworkingFingerprint
import com.better.tmdbtest.ui.main.adapter.fingerprints.PressTryAgain

class FilmsFavoriteFragment : Fragment(), AddToFavorite, PressTryAgain {

    companion object {
        fun newInstance() = FilmsFavoriteFragment()
    }

    private var _binding: FragmentFilmsFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val filmAdapter = FilmAdapter(getFingerprints())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        binding.recyclerView.apply {
            adapter = filmAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.favoriteFilmsLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.apply {
                    recyclerView.visibility = View.INVISIBLE
                    emptyListError.root.visibility = View.VISIBLE
                    emptyListError.btnTryAgain.visibility = View.INVISIBLE
                }
            } else {
                binding.apply {
                    recyclerView.visibility = View.VISIBLE
                    emptyListError.root.visibility = View.INVISIBLE
                }
                filmAdapter.submitList(it.toList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFingerprints() = listOf(
        FilmFingerprint(this),
        NetworkingFingerprint(this)
    )

    override fun addToFavorite(film: Film, position: Int) {
        viewModel.addToFavorite(film)
    }
    override fun tryAgain() {
    }
}