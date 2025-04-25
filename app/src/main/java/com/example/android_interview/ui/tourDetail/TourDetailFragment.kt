package com.example.android_interview.ui.tourDetail

import android.content.ActivityNotFoundException
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.android_interview.Global
import com.example.android_interview.R
import com.example.android_interview.base.BaseFragment
import com.example.android_interview.databinding.FragmentTourDetailBinding
import com.example.android_interview.extension.parcelable
import com.example.android_interview.extension.viewBinding
import com.example.android_interview.model.loading.Loading
import com.example.android_interview.model.response.ScenicSpotResponse
import com.example.android_interview.ui.MainActivity
import com.example.android_interview.ui.webView.WebViewFragmentArgs
import com.example.android_interview.util.ActionUtil
import com.example.android_interview.util.types.SharedPreferencesType
import timber.log.Timber

enum class  TourDetailFragmentArgs(val key: String) {
    ScenicSpot("scenicSpot")
}

class TourDetailFragment : BaseFragment<FragmentTourDetailBinding>() {

    override val binding: FragmentTourDetailBinding by viewBinding()

    override fun FragmentTourDetailBinding.initView() {
        onBinding()
        Loading.hide()
    }

    private  fun onBinding() {
        val activity = activity as? MainActivity
        val isEnglish = activity?.prefs?.getBoolean(SharedPreferencesType.LANG.key, false) ?: false
        val data: ScenicSpotResponse? = requireArguments().parcelable<ScenicSpotResponse>(TourDetailFragmentArgs.ScenicSpot.key)
        binding.apply {
            fragmentTourDetailImg.load(data?.photoUrl1) {
                error(R.drawable.image_empty)
            }
            fragmentTourDetailTitle.text = if (isEnglish) data?.nameEn else data?.nameZh
            fragmentTourDetailContent.text =  if (isEnglish) data?.descriptionEn else data?.descriptionZh
            fragmentTourDetailWebLink.text = mainActivity?.localizedResources?.getString(R.string.tour_web_url_title)
            fragmentTourDetailWebLink.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            fragmentTourDetailMapTv.text = mainActivity?.localizedResources?.getString(R.string.tour_map_title)

            fragmentTourDetailImg.post {
                val imageHeight = fragmentTourDetailImg.height
                val marginTop = (imageHeight - 50)
                val layoutParams = fragmentTourDetailTextClayout.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topMargin = marginTop
                fragmentTourDetailTextClayout.layoutParams = layoutParams
            }

            fragmentTourDetailBackBtn.setOnClickListener { findNavController().popBackStack() }
            fragmentTourDetailMapTv.setOnClickListener { onOpenMap(data) }
            fragmentTourDetailWebLink.setOnClickListener { onOpenWeb(data?.websiteUrl) }
        }
    }

    private fun onOpenMap(data: ScenicSpotResponse?) {
        if (data != null) {
            if (data.mapUrl.isNotEmpty()) {
                onOpenWeb(data.mapUrl)
            } else {
                try {
                    val uri =  Global.getMapByGEO(data.nameZh, data.latitude, data.longitude)
                    val intent = ActionUtil.onGoogleMap(uri)
                    startActivity(intent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "找不到可以開啟地圖的應用程式", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Timber.w("not map data.")
        }
    }

    private fun onOpenWeb(url: String?) {
        if (url != null) {
            Loading.show()
            val bundle = Bundle().apply { putString(WebViewFragmentArgs.URL.key, url) }
            findNavController().navigate(R.id.webViewFragment, bundle)
        }
    }
}