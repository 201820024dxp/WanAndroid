package com.wanandroid.app.ui.home.child.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentHomeChildExploreBinding
import com.wanandroid.app.logic.model.Banner
import com.wanandroid.app.ui.home.item.HomeBannerAdapter
import com.wanandroid.app.ui.web.WebActivity
import com.youth.banner.indicator.CircleIndicator

/**
 * 首页探索页面
 */
class ExploreFragment : BaseFragment<FragmentHomeChildExploreBinding>() {

    companion object {
        const val KEY_CHILD_EXPLORE_TAB_PARCELABLE = "key_child_explore_tab_parcelable"
    }

    private lateinit var bannerAdapter: HomeBannerAdapter

    val viewModel: ExploreViewModel by lazy { ExploreViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeChildExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 初始化视图
        // banner加载事件
        viewModel.bannerList.observe(viewLifecycleOwner) { banners ->
            if (banners.isNotEmpty()) {
                binding.exploreBanner.setAdapter(HomeBannerAdapter(banners))
                    .addBannerLifecycleObserver(this)
                    .setIndicator(CircleIndicator(this.context))
                    .setOnBannerListener { data, position ->
                        // 处理Banner点击事件
                        onBannerItemClick(data as Banner, position)
                    }
            }
        }
        // article加载事件
        viewModel.getBanner()

        // 初始化事件

        if (savedInstanceState == null) {
            onRefresh()
        }
    }

    private fun onBannerItemClick(data: Banner, position: Int) {
        WebActivity.loadUrl(this.requireContext(), data.url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun onRefresh() {
        // 刷新 banner 与 articleList
    }
}