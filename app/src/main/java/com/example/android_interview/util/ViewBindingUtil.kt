package com.example.android_interview.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.viewbinding.ViewBinding

object ViewBindingUtil {

    private val inflateCache =
        mutableMapOf<Triple<Class<*>, Int, Class<out ViewBinding>>, InflateViewBinding<ViewBinding>>()
    private val bindCache =
        mutableMapOf<Triple<Class<*>, Int, Class<out ViewBinding>>, BindViewBinding<ViewBinding>>()

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    internal fun <T : ViewBinding> getInflate(
        targetClass: Class<*>,
        targetHashCode: Int,
        viewBindingClass: Class<T>
    ): InflateViewBinding<T> = inflateCache.getOrPut(
        Triple(
            targetClass,
            targetHashCode,
            viewBindingClass
        )
    ) { InflateViewBinding(viewBindingClass) } as InflateViewBinding<T>

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    internal fun <T : ViewBinding> getBind(
        targetClass: Class<*>,
        targetHashCode: Int,
        viewBindingClass: Class<T>
    ): BindViewBinding<T> =
        bindCache.getOrPut(Triple(targetClass, targetHashCode, viewBindingClass)) {
            BindViewBinding(
                viewBindingClass
            )
        } as BindViewBinding<T>

    fun <T, R : ViewBinding> remove(
        targetClass: Class<T>,
        targetHashCode: Int,
        viewBindingClass: Class<R>
    ) {
        val key = Triple(targetClass, targetHashCode, viewBindingClass)
        if (inflateCache.containsKey(key)) inflateCache.remove(key)
        if (bindCache.containsKey(key)) bindCache.remove(key)
    }

    fun clear() {
        inflateCache.clear()
        bindCache.clear()
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class InflateViewBinding<out VB : ViewBinding>(viewBindingClass: Class<VB>) {

    private var instance: VB? = null

    private val inflateViewBinding = viewBindingClass.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )

    @Suppress("UNCHECKED_CAST")
    fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup? = null,
        attachToParent: Boolean = false
    ): VB =
        instance ?: (inflateViewBinding(
            null,
            layoutInflater,
            parent,
            attachToParent
        ) as VB).apply { instance = this }
}

/**
 * Wrapper of ViewBinding.bind(View)
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BindViewBinding<out VB : ViewBinding>(viewBindingClass: Class<VB>) {

    private var instance: VB? = null

    private val bindViewBinding = viewBindingClass.getMethod("bind", View::class.java)

    @Suppress("UNCHECKED_CAST")
    fun bind(view: View): VB =
        instance ?: (bindViewBinding(null, view) as VB).apply { instance = this }
}