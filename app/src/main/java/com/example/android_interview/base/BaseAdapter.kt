package com.example.android_interview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : BaseViewHolder<Any?>, R> : RecyclerView.Adapter<VH>() {

    private var _data: MutableList<T> = mutableListOf()
    val data: List<T> = _data

    protected var lastClickedPosition = -1

    private var limitedCount = -1

    open var onItemClickListener: ((View?, Int, T) -> Unit)? = null

    abstract val viewHolderInjection: ViewHolderInjection<VH, R>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        viewHolderInjection.getViewHolder(viewHolderInjection.getViewInject(parent, viewType), viewType)

    override fun getItemCount(): Int = _data.size

    protected fun isLastClickedPositionNull() = lastClickedPosition == -1

    protected fun clearLastClickedPosition() {
        if (lastClickedPosition == -1) return
        lastClickedPosition = -1
        notifyItemRangeChanged(0, _data.size)
    }

    protected open fun getCurrentData() =
        if (lastClickedPosition < 0 || lastClickedPosition >= itemCount) null else data[lastClickedPosition]

    protected fun View.updateSelectedState(position: Int) {
        isSelected = lastClickedPosition == position
    }

    fun reloadData(list: List<T>?) {
        _data.clear()
        list?.let { _data.addAll(it) }
        notifyItemRangeChanged(0, _data.size)
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        _data.removeAt(position)
        notifyItemChanged(position)
    }

    fun limitedItemCount(limited: Int) {
        limitedCount = limited
    }
}

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view)

interface ViewHolderInjection<T: BaseViewHolder<Any?>, R> {
    fun getViewInject(parent: ViewGroup, viewType: Int): R

    fun getViewHolder(r: R, viewType: Int): T
}