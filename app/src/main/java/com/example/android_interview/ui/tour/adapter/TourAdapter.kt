package com.example.android_interview.ui.tour.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.example.android_interview.R
import com.example.android_interview.base.BaseAdapter
import com.example.android_interview.base.BaseViewHolder
import com.example.android_interview.databinding.ItemTourBinding
import com.example.android_interview.model.response.ScenicSpotResponse

class TourAdapter(
    private val onItemClick: (data: ScenicSpotResponse) -> Unit
) : BaseAdapter<ScenicSpotResponse, ItemTourBinding>() {

    private var isEnglish: Boolean = false

    override val bindingInflater: (LayoutInflater, ViewGroup?, attachToParent: Boolean, viewType: Int) -> ItemTourBinding
        get() = { layoutInflater, viewGroup, attachToParent, _ ->
            ItemTourBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<ItemTourBinding>, position: Int) {
        val result = data[position]
        holder.binding.apply {
            itemTourImg.load(result.photoUrl1) {
                error(R.drawable.image_empty)
            }
            itemTourTitleTv.text = if (isEnglish) result.nameEn else result.nameZh
            itemTourContentTv.text = if (isEnglish) result.descriptionEn else result.descriptionZh
            itemTourClayout.setOnClickListener {
                onItemClick.invoke(data[position])
            }
        }
    }

    fun setEnglish(isEnglish: Boolean) {
        this.isEnglish = isEnglish
        notifyDataSetChanged()
    }
}