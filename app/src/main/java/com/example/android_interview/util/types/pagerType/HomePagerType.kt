package com.example.android_interview.util.types.pagerType

import androidx.annotation.StringRes
import com.example.android_interview.R

enum class HomePagerType(@StringRes val stringRes: Int) {
    Flower(R.string.flower_view),
    Tour(R.string.tour_place);

    companion object {
        fun fromOrdinal(ordinal: Int): HomePagerType? =
            entries.associateBy(HomePagerType::ordinal)[ordinal]
    }
}