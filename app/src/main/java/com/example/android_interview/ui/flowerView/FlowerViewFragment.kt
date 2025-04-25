package com.example.android_interview.ui.flowerView

import android.content.ActivityNotFoundException
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_interview.Global
import com.example.android_interview.R
import com.example.android_interview.base.BaseFragment
import com.example.android_interview.databinding.FragmentFlowerViewBinding
import com.example.android_interview.extension.viewBinding
import com.example.android_interview.model.loading.Loading
import com.example.android_interview.model.response.AttractionResponse
import com.example.android_interview.ui.flowerView.adapter.FlowerListAdapter
import com.example.android_interview.util.ActionUtil
import com.example.android_interview.util.HeaderItemDecoration
import com.example.android_interview.viewModel.flowerView.FlowerViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class FlowerViewFragment : BaseFragment<FragmentFlowerViewBinding>() {

    private val viewModel: FlowerViewModel by viewModels()

    private val listAdapter: FlowerListAdapter by lazy {
        FlowerListAdapter(
            resources = mainActivity?.localizedResources,
            onAddressClick = ::onOpenGoogleMap
        )
    }

    override val binding: FragmentFlowerViewBinding by viewBinding()

    override fun FragmentFlowerViewBinding.initView() {
        Timber.i("initView: $this")
        onBinding()
        onObserver()
        onFetchData()
    }

    private fun onBinding() {
        binding.apply {
            fragmentFlowerRcycler.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = listAdapter
                addItemDecoration(HeaderItemDecoration(Global.HEADER_HEIGHT))
            }

            fragmentFlowerSearchEt.addTextChangedListener {
                val input = it.toString()
                viewModel.filter(input)
            }

            fragmentFlowerSwipeRefresh.setOnRefreshListener {
                val searchMode = fragmentFlowerLlayout.visibility == View.VISIBLE
                if (!searchMode) {
                    Loading.show()
                    viewModel.refreshAttractions()
                } else {
                    fragmentFlowerSwipeRefresh.isRefreshing = false
                }
            }

            fragmentFlowerRcycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    val searchMode = fragmentFlowerLlayout.visibility == View.VISIBLE
                    if (lastVisibleItemPosition == totalItemCount - 1 && !Loading.state.value && !searchMode) {
                        Loading.show()
                        viewModel.fetchAttractions()
                    }
                }
            })

            fragmentFlowerCommentMlayout.setTransitionListener(object :
                MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout, startId: Int, endId: Int
                ) {/* Not do anything */
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float
                ) {/* Not do anything */
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    if (currentId == R.id.end) {
                        binding.fragmentFlowerSearchEt.requestFocus()
                        val imm =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.showSoftInput(
                            binding.fragmentFlowerSearchEt,
                            InputMethodManager.SHOW_IMPLICIT
                        )
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float
                ) {/* Not do anything */
                }
            })
        }
    }

    private fun onObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.attractionsData.collect {
                        if (it.isEmpty()) {
                            binding.apply {
                                fragmentFlowerDataBaseClayout.visibility = View.GONE
                                fragemntFlowerEmptyLlayout.visibility = View.VISIBLE
                            }
                            Loading.hide()
                            return@collect
                        }
                        binding.apply {
                            fragmentFlowerDataBaseClayout.visibility = View.VISIBLE
                            fragemntFlowerEmptyLlayout.visibility = View.GONE
                            fragmentFlowerSwipeRefresh.isRefreshing = false
                        }
                        listAdapter.reloadData(it)
                        Loading.hide()
                    }
                }
            }
        }
    }

    private fun onFetchData() {
        if (Loading.state.value) return
        Loading.show()
        viewModel.fetchAttractions()
    }

    private fun onOpenGoogleMap(data: AttractionResponse) {
        try {
            val uri = Global.getMapByUrl(data.address)
            val intent = ActionUtil.onGoogleMap(uri)
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                mainActivity?.localizedResources?.getString(R.string.can_not_open_map_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}