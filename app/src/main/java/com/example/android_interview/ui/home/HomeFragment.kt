package com.example.android_interview.ui.home

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android_interview.R
import com.example.android_interview.base.BaseFragment
import com.example.android_interview.databinding.FragmentHomeBinding
import com.example.android_interview.extension.viewBinding
import com.example.android_interview.ui.home.adapter.HomePagerAdapter
import com.example.android_interview.util.types.SharedPreferencesType
import com.example.android_interview.viewModel.home.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.util.Locale


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val tabRes = listOf(R.string.flower_view, R.string.tour_place)

    private val viewModel: HomeViewModel by activityViewModels()

    private var mediator: TabLayoutMediator? = null

    override val binding: FragmentHomeBinding by viewBinding()

    private val pagerAdapter by lazy {
        HomePagerAdapter(this@HomeFragment)
    }

    override fun FragmentHomeBinding.initView() {
        onObserver()
        onBinding()
        onFetchData()
    }

    private fun onObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEnglish.collect {
                    binding.apply {
                        fragmentHomeLanguageBtn.text = if (it) "EN" else "中"
                    }
                }
            }
        }
    }

    private fun onBinding() {
        binding.apply {
            mediator = TabLayoutMediator(fragmentHomeTabLayout, fragmentHomeVp) { tab, position ->
                tab.text = mainActivity?.localizedResources?.getString(tabRes[position])
            }
            fragmentHomeVp.apply {
                isSaveEnabled = false
                adapter = pagerAdapter
                mediator!!.attach()
            }
            fragmentHomeLanguageBtn.setOnClickListener {
                onTriggerLanguage()
                resetMediator()
            }
        }
    }

    private fun resetMediator() {
        mediator?.detach()
        mediator?.attach()
    }

    private fun onFetchData() {
        val language = mainActivity?.prefs?.getBoolean(SharedPreferencesType.LANG.key, false)
        viewModel.setLanguage(language ?: false)
    }

    private fun onTriggerLanguage() {
        val nowState = viewModel.triggerLanguage()
        mainActivity?.prefs?.edit()?.putBoolean(SharedPreferencesType.LANG.key, nowState)?.apply()
        val locale = Locale(if (nowState) "en" else "zh")
        mainActivity?.updateLocalizedLanguage(locale)
    }

}