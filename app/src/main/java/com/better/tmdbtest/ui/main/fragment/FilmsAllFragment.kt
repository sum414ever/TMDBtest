package com.better.tmdbtest.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.better.tmdbtest.databinding.FragmentFilmsAllBinding
import com.better.tmdbtest.domain.entity.Film
import com.better.tmdbtest.ui.main.MainViewModel
import com.better.tmdbtest.ui.main.adapter.FilmAdapter
import com.better.tmdbtest.ui.main.adapter.fingerprints.AddToFavorite
import com.better.tmdbtest.ui.main.adapter.fingerprints.FilmFingerprint
import com.better.tmdbtest.ui.main.adapter.fingerprints.NetworkingFingerprint
import com.better.tmdbtest.ui.main.adapter.fingerprints.PressTryAgain
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilmsAllFragment : Fragment(), AddToFavorite, PressTryAgain {
    private var _binding: FragmentFilmsAllBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val filmAdapter = FilmAdapter(getFingerprints())
    private var isNetworkingOrError = false

    companion object {
        fun newInstance() = FilmsAllFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.apply {
                animation = null
                adapter = filmAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val llm = recyclerView.layoutManager as LinearLayoutManager
                        if (llm.findLastVisibleItemPosition() > filmAdapter.itemCount - 4
                            && !viewModel.viewState.value?.isNetworking!!
                        ) {
                            if (!isNetworkingOrError) {
                                viewModel.pageIndex++
                                viewModel.loadFilmsPage()
                            }
                        }

                        if (llm.findLastVisibleItemPosition() > 20) binding.ibScrollUp.visibility =
                            View.VISIBLE
                        else binding.ibScrollUp.visibility = View.INVISIBLE
                    }
                })
            }

            swipeRefreshLayout.apply {
                setProgressViewOffset(true, 0, 300)
                setOnRefreshListener {
                    viewModel.refreshList()
                    swipeRefreshLayout.isRefreshing = false
                }
            }

            emptyListError.btnTryAgain.setOnClickListener {
                viewModel.loadFilmsPage()
            }

            binding.ibScrollUp.setOnClickListener {
                scrollToTop()
            }
        }

        observeViewModel()
    }

    private fun scrollToTop() {
        val llm = binding.recyclerView.layoutManager as LinearLayoutManager
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateTimeForScrolling(dx: Int): Int {
                return 200
            }
        }
        smoothScroller.targetPosition = 0
        llm.startSmoothScroll(smoothScroller)
    }

    private fun observeViewModel() {
        viewModel.filmsLiveData.observe(viewLifecycleOwner) {

            if (it.isEmpty()) {
                binding.apply {
                    recyclerView.visibility = View.INVISIBLE
                    swipeRefreshLayout.visibility = View.GONE
                    emptyListError.root.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    recyclerView.visibility = View.VISIBLE
                    swipeRefreshLayout.visibility = View.VISIBLE
                    emptyListError.root.visibility = View.INVISIBLE
                }
                filmAdapter.submitList(it)

                filmAdapter.notifyDataSetChanged()
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
           isNetworkingOrError = !it.errorMessage.isNullOrEmpty() || !it.errorMessage.isNullOrEmpty()
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

    override fun addToFavorite(film: Film, position: Int) =
        viewModel.addToFavorite(film)


    override fun tryAgain() {
        viewModel.pressTryAgain()

    }
}

