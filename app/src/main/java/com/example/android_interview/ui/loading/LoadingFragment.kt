package com.example.android_interview.ui.loading

import com.example.android_interview.base.BaseFragment
import com.example.android_interview.databinding.FragmentLoadingBinding
import com.example.android_interview.extension.viewBinding
import timber.log.Timber

class LoadingFragment : BaseFragment<FragmentLoadingBinding>() {

    override val binding: FragmentLoadingBinding by viewBinding()

    override fun FragmentLoadingBinding.initView() {
        Timber.i("initView: $this")
    }
}