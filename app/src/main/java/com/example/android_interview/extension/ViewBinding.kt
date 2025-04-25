package com.example.android_interview.extension

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.RestrictTo
import androidx.core.view.doOnDetach
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.example.android_interview.util.ViewBindingUtil

enum class ViewBindingType {
    BIND,
    INFLATE
}

/// activity
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(type: ViewBindingType = ViewBindingType.INFLATE) =
    ActivityDelegateProperty<ComponentActivity, T> { initViewBinding(type) }

inline fun <reified T : ViewBinding> ComponentActivity.initViewBinding(type: ViewBindingType = ViewBindingType.INFLATE) =
    viewBinding(T::class.java, type)

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun <T : ViewBinding> ComponentActivity.viewBinding(
    viewBindingClass: Class<T>,
    type: ViewBindingType = ViewBindingType.INFLATE
): T {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            destroyViewBinding(viewBindingClass)
        }
    })
    return when (type) {
        ViewBindingType.BIND -> ViewBindingUtil.getBind(this::class.java, hashCode(), viewBindingClass)
            .bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0))
        ViewBindingType.INFLATE -> ViewBindingUtil.getInflate(
            this::class.java,
            hashCode(),
            viewBindingClass
        ).inflate(layoutInflater)
    }
}

/// fragment
inline fun <reified T : ViewBinding> Fragment.viewBinding(
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
    type: ViewBindingType = ViewBindingType.INFLATE
) = FragmentDelegateProperty<Fragment, T> { initViewBinding(parent, attachToParent, type) }

inline fun <reified T : ViewBinding> Fragment.initViewBinding(
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
    type: ViewBindingType = ViewBindingType.INFLATE
) = viewBinding(T::class.java, parent, attachToParent, type)

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingClass: Class<T>,
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
    type: ViewBindingType = ViewBindingType.INFLATE
): T {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            destroyViewBinding(viewBindingClass)
        }
    })
    return when (type) {
        ViewBindingType.BIND -> ViewBindingUtil.getBind(this::class.java, hashCode(),viewBindingClass)
            .bind(requireView())
        ViewBindingType.INFLATE -> ViewBindingUtil.getInflate(
            this::class.java,
            hashCode(),
            viewBindingClass
        ).inflate(layoutInflater, parent, attachToParent)
    }
}

/// view
inline fun <reified T : ViewBinding> ViewGroup.viewBinding(
    type: ViewBindingType = ViewBindingType.INFLATE
) = LazyDelegateProperty<ViewGroup, T> { initViewBinding(type) }

inline fun <reified T : ViewBinding> ViewGroup.initViewBinding(
    type: ViewBindingType = ViewBindingType.INFLATE
) = viewBinding(T::class.java, type)

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun <T : ViewBinding> ViewGroup.viewBinding(viewBindingClass: Class<T>, type: ViewBindingType = ViewBindingType.INFLATE): T {
    doOnDetach { destroyViewBinding(viewBindingClass) }
    return when (type) {
        ViewBindingType.BIND -> ViewBindingUtil.getBind(this::class.java, hashCode(), viewBindingClass)
            .bind(this)
        ViewBindingType.INFLATE -> ViewBindingUtil.getInflate(
            this::class.java,
            hashCode(),
            viewBindingClass
        ).inflate(LayoutInflater.from(context), this, true)
    }
}

fun ComponentActivity.destroyViewBinding(viewBindingClass: Class<out ViewBinding>) =
    ViewBindingUtil.remove(this::class.java, hashCode(), viewBindingClass)

fun Fragment.destroyViewBinding(viewBindingClass: Class<out ViewBinding>) =
    ViewBindingUtil.remove(this::class.java, hashCode(), viewBindingClass)

fun ViewGroup.destroyViewBinding(viewBindingClass: Class<out ViewBinding>) =
    ViewBindingUtil.remove(this::class.java, hashCode(), viewBindingClass)