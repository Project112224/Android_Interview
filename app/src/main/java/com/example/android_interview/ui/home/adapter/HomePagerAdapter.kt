package com.example.android_interview.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android_interview.ui.flowerView.FlowerViewFragment
import com.example.android_interview.ui.home.HomeFragment
import com.example.android_interview.ui.tour.TourFragment
import com.example.android_interview.util.types.pagerType.HomePagerType

class HomePagerAdapter(parent: HomeFragment) : FragmentStateAdapter(parent) {
    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int = HomePagerType.entries.size

    override fun createFragment(position: Int): Fragment {
        return when (HomePagerType.fromOrdinal(position)) {
            HomePagerType.Flower -> {
                val fragment = FlowerViewFragment()
                fragments.add(fragment)
                fragment
            }
            HomePagerType.Tour -> {
                val fragment = TourFragment()
                fragments.add(fragment)
                fragment
            }
            null -> TODO()
        }
    }
}