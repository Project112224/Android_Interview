package com.example.android_interview.extension

import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ActivityDelegateProperty<A : ComponentActivity, T>(
    injection: (A) -> T
) : LifecycleDelegateProperty<A, T>(injection) {
    override fun getLifecycleOwner(thisRef: A) = thisRef
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
class FragmentDelegateProperty<F : Fragment, T>(
    injection: (F) -> T
) : LifecycleDelegateProperty<F, T>(injection) {
    override fun getLifecycleOwner(thisRef: F) = thisRef.viewLifecycleOwner
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class LifecycleDelegateProperty<in R, T>(
    private val injection: (R) -> T
) : LazyDelegateProperty<R, T>(injection) {

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        t?.let { return it }

        val result = injection(thisRef)
        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver())
            t = result
        }
        return result
    }

    @MainThread
    override fun clear() {
        mainHandler.post { t = null }
    }

    private inner class ClearOnDestroyLifecycleObserver : DefaultLifecycleObserver {
        @MainThread
        override fun onDestroy(owner: LifecycleOwner): Unit = clear()
    }

    private companion object {
        private val mainHandler = Handler(Looper.getMainLooper())
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
open class LazyDelegateProperty<in R, T>(
    private val injection: (R) -> T
) : DelegateProperty<R, T> {

    internal var t: T? = null

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T = t ?: injection(thisRef).apply { t = this }

    @MainThread
    override fun clear() {
        t = null
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
interface DelegateProperty<in R, T> : ReadOnlyProperty<R, T> {
    @MainThread
    fun clear()
}