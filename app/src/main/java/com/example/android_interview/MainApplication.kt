package com.example.android_interview

import android.app.Activity
import android.app.Application
import android.os.Bundle

class MainApplication: Application() {

    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks()
    }

    private fun registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                // Not do something
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                currentActivity = null
            }

            override fun onActivityStopped(activity: Activity) {
                // Not do something
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                // Not do something
            }

            override fun onActivityDestroyed(activity: Activity) {
                currentActivity = null
            }
        })
    }
}