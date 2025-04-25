package com.example.android_interview.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.android_interview.ViewHolderInjection

abstract class BaseAdapter<T, VB : ViewBinding> :
    com.example.android_interview.BaseAdapter<T, BaseViewHolder<VB>, VB>() {
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, attachToParent: Boolean, viewType: Int) -> VB

    override val viewHolderInjection: ViewHolderInjection<BaseViewHolder<VB>, VB> =
        object : ViewHolderInjection<BaseViewHolder<VB>, VB> {
            override fun getViewInject(parent: ViewGroup, viewType: Int): VB =
                bindingInflater.invoke(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                    viewType
                )

            override fun getViewHolder(r: VB, viewType: Int): BaseViewHolder<VB> =
                object : BaseViewHolder<VB>(r) {
                    init {
                        onItemClickListener?.let { func ->
                            r.root.setOnClickListener {
                                val position = adapterPosition
                                if (position >= data.size || position < 0) return@setOnClickListener
                                lastClickedPosition = position
                                func.invoke(it, position, data[position])
                                notifyItemChanged(position)
                            }
                        }
                    }
                }
        }
}

abstract class BaseViewHolder<VB>(open val binding: VB) :
    com.example.android_interview.BaseViewHolder<Any?>(binding.root) where VB : ViewBinding