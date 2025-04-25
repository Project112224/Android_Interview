package com.example.android_interview.ui

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android_interview.BuildConfig
import com.example.android_interview.R
import com.example.android_interview.base.BaseActivity
import com.example.android_interview.databinding.ActivityMainBinding
import com.example.android_interview.extension.viewBinding
import com.example.android_interview.util.GlobalEventBus
import com.example.android_interview.util.types.SharedPreferencesType
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale


class MainActivity: BaseActivity<ActivityMainBinding>() {

    val prefs by lazy {
        binding.root.context?.getSharedPreferences(SharedPreferencesType.APP_PREFS.key, Context.MODE_PRIVATE)
    }

    var localizedResources : Resources? = null

    override val binding: ActivityMainBinding by viewBinding()

    override fun ActivityMainBinding.initView() {
        fetchLanguage()
        onObserver()
        renderDebugSetting()
    }

    override fun onChanged(value: StateFlow<String?>) {
        // Not yet implemented
    }

    override fun showLoading() {
        super.showLoading()
        binding.activityLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.activityLoading.visibility = View.GONE
        super.hideLoading()
    }

    private fun onObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                GlobalEventBus.events.filterNotNull().collect { event ->
                    when (event) {
                        is GlobalEventBus.GlobalEvent.Timeout -> showTimeoutDialog()
                        is GlobalEventBus.GlobalEvent.NoNetwork -> showNoNetworkDialog()
                        is GlobalEventBus.GlobalEvent.HttpError -> showHttpErrorDialog(event.code)
                    }
                }
            }
        }
    }

    fun updateLocalizedLanguage(locale: Locale) {
        val resources: Resources = binding.root.context.resources
        Locale.setDefault(locale)
        val conf: Configuration = resources.configuration
        conf.setLocale(locale)
        val context = baseContext.createConfigurationContext(conf)
        localizedResources = context.resources
    }

    private fun renderDebugSetting() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun fetchLanguage() {
        val language = prefs?.getBoolean(SharedPreferencesType.LANG.key, false)
        val locale = Locale(if (language == true) "en" else "zh")
        updateLocalizedLanguage(locale)
    }

    private fun showTimeoutDialog() {
        AlertDialog.Builder(this)
            .setTitle(localizedResources?.getString(R.string.error))
            .setMessage(localizedResources?.getString(R.string.timeout_message))
            .setPositiveButton(localizedResources?.getString(R.string.confirm), null)
            .show()
    }

    private fun showNoNetworkDialog() {
        AlertDialog.Builder(this)
            .setTitle(localizedResources?.getString(R.string.error))
            .setMessage(localizedResources?.getString(R.string.no_network_message))
            .setPositiveButton(localizedResources?.getString(R.string.confirm), null)
            .show()
    }

    private fun showHttpErrorDialog(code: Int?) {
        AlertDialog.Builder(this)
            .setTitle(localizedResources?.getString(R.string.error))
            .setMessage(localizedResources?.getString(R.string.system_error_message, "$code"))
            .setPositiveButton(localizedResources?.getString(R.string.confirm), null)
            .show()
    }
}