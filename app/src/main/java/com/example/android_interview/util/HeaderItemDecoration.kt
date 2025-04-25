package com.example.android_interview.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class HeaderItemDecoration(
    private val space: Int,
    private val onlyFirst: Boolean = true
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        if (onlyFirst) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space
            }
        } else {
            outRect.top = space
        }
    }
}