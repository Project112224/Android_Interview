package com.example.android_interview.ui.flowerView.adapter

import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.findFragment
import com.example.android_interview.R
import com.example.android_interview.base.BaseAdapter
import com.example.android_interview.base.BaseViewHolder
import com.example.android_interview.databinding.ItemFlowerBinding
import com.example.android_interview.extension.isInBloomByRange
import com.example.android_interview.model.response.AttractionResponse
import com.example.android_interview.ui.flowerView.FlowerViewFragment

class FlowerListAdapter(
    private val resources: Resources?,
    private val onAddressClick: (data: AttractionResponse) -> Unit
) : BaseAdapter<AttractionResponse, ItemFlowerBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, attachToParent: Boolean, viewType: Int) -> ItemFlowerBinding
        get() = { layoutInflater, viewGroup, attachToParent, _ ->
            ItemFlowerBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<ItemFlowerBinding>, position: Int) {
        val result = data[position]
        val isInBloom = result.season.isInBloomByRange()
        val colorRes =  if (isInBloom) R.color.green else R.color.gray
        holder.binding.apply {
            itemFlowerTitle.text = result.plate
            itemFlowerDate.text = result.season
            itemFlowerType.text = resources?.getString(R.string.flower_type, result.flowerName)
            itemFlowerAddress.text = result.address
            itemFlowerAddress.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            val dateTextColor = ContextCompat.getColor(root.context, colorRes)
            itemFlowerDate.setTextColor(dateTextColor)
            itemFlowerAddress.setOnClickListener {
                onAddressClick.invoke(result)
            }
        }
    }
}