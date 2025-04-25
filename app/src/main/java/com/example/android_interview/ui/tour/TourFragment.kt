package com.example.android_interview.ui.tour

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_interview.Global
import com.example.android_interview.R
import com.example.android_interview.base.BaseFragment
import com.example.android_interview.databinding.FragmentTourBinding
import com.example.android_interview.extension.viewBinding
import com.example.android_interview.model.loading.Loading
import com.example.android_interview.model.response.ScenicSpotResponse
import com.example.android_interview.ui.MainActivity
import com.example.android_interview.ui.tour.adapter.TourAdapter
import com.example.android_interview.ui.tourDetail.TourDetailFragmentArgs
import com.example.android_interview.util.HeaderItemDecoration
import com.example.android_interview.util.types.SharedPreferencesType
import com.example.android_interview.viewModel.home.HomeViewModel
import com.example.android_interview.viewModel.tour.TourViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class TourFragment : BaseFragment<FragmentTourBinding>() {

    private val viewModel: TourViewModel by viewModels()

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val tourAdapter by lazy {
        TourAdapter(onItemClick = ::onItemClick)
    }

    override val binding: FragmentTourBinding by viewBinding()

    override fun FragmentTourBinding.initView() {
        onObserver()
        onListener()
        onFetchData()
    }

    private fun onObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.tourInfo.filterNotNull().collect {
                        binding.apply {
                            if (it.isEmpty()) {
                                fragmentTourEmptyLlayout.visibility = View.VISIBLE
                                fragmentTourRcycler.visibility = View.GONE
                                Loading.hide()
                                fragmentTourSwipeRefresh.isRefreshing = false
                                return@collect
                            }
                            fragmentTourEmptyLlayout.visibility = View.GONE
                            fragmentTourRcycler.visibility = View.VISIBLE
                        }
                        tourAdapter.reloadData(it)
                        R.string.tour_place
                        Loading.hide()
                        binding.fragmentTourSwipeRefresh.isRefreshing = false
                    }
                }

                launch {
                    homeViewModel.isEnglish.collect {
                        tourAdapter.setEnglish(it)
                    }
                }
            }
        }
    }

    private fun onListener() {
        binding.apply {
            fragmentTourRcycler.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = tourAdapter
                addItemDecoration(HeaderItemDecoration(Global.HEADER_HEIGHT))
            }

            fragmentTourSwipeRefresh.setOnRefreshListener {
                viewModel.fetchTourInfo()
            }
        }
    }

    private fun onFetchData() {
        Loading.show()
        viewModel.fetchTourInfo()
        onFetchLanguage()
    }

    private  fun onFetchLanguage() {
        val activity = activity as? MainActivity
        val language = activity?.prefs?.getBoolean(SharedPreferencesType.LANG.key, false)
        homeViewModel.setLanguage(language ?: false)
    }

    private fun onItemClick(data: ScenicSpotResponse) {
        val bundle = Bundle().apply { putParcelable(TourDetailFragmentArgs.ScenicSpot.key, data) }
        findNavController().navigate(R.id.tourDetailFragment, bundle)
    }

}