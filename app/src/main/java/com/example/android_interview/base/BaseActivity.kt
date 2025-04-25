package com.example.android_interview.base

import android.os.Bundle

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.android_interview.model.loading.Loading
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseActivity<T> : AppCompatActivity(),
    Observer<StateFlow<String?>> where T : ViewBinding {

    abstract val binding: T

    abstract fun T.initView()

    open fun onAPIError() {
        // nothing
    }

    open fun showLoading() {
        // nothing
    }

    open fun hideLoading() {
        // nothing
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        binding.initView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    Loading.state.collect { isLoading ->
                        if (isLoading) showLoading() else hideLoading()
                    }
                }
            }
        }
    }

}