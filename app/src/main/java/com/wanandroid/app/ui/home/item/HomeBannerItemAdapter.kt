package com.wanandroid.app.ui.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.databinding.ItemHomeBannerLayoutBinding
import com.wanandroid.app.logic.model.Banner
import com.wanandroid.app.ui.home.child.explore.ExploreFragment
import com.youth.banner.indicator.CircleIndicator

class HomeBannerItemAdapter(var banners: List<Banner>, val fragment: ExploreFragment)
    : RecyclerView.Adapter<HomeBannerItemAdapter.ViewHolder>() {

    val bannerAdapter = HomeBannerAdapter(banners)

    class ViewHolder(binding: ItemHomeBannerLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeBannerLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        binding.exploreBanner.setAdapter(bannerAdapter)
            .addBannerLifecycleObserver(fragment)
            .setIndicator(CircleIndicator(fragment.context))
            .setOnBannerListener { data, position ->
                // 处理Banner点击事件
                fragment.onBannerItemClick(data as Banner, position)
            }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bannerAdapter.setDatas(banners)
    }

    override fun getItemCount(): Int = 1

}