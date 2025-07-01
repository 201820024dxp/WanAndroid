package com.wanandroid.app.ui.home.child.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentHomeChildExploreBinding
import com.wanandroid.app.logic.model.BannerResponse
import com.wanandroid.app.ui.home.item.HomeBannerAdapter
import com.wanandroid.app.ui.web.WebActivity
import com.youth.banner.indicator.CircleIndicator

class ExploreFragment : BaseFragment<FragmentHomeChildExploreBinding>() {

    companion object {
        const val KEY_CHILD_EXPLORE_TAB_PARCELABLE = "key_child_explore_tab_parcelable"

//        fun newInstance(tabBean: HomeTabBean): ExploreFragment {
//            val fragment = ExploreFragment()
//            val args = Bundle().apply {
//                putParcelable(KEY_CHILD_EXPLORE_TAB_PARCELABLE, tabBean)
//            }
//            fragment.arguments = args
//            return fragment
//        }
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
        // 加载banner
        viewModel.bannerList.observe(viewLifecycleOwner) { banners ->
            if (banners.isNotEmpty()) {
                binding.exploreBanner.setAdapter(HomeBannerAdapter(banners))
                    .addBannerLifecycleObserver(this)
                    .setIndicator(CircleIndicator(this.context))
                    .setOnBannerListener { data, position ->
                        // 处理Banner点击事件
                        onBannerItemClick(data as BannerResponse.Banner, position)
                    }
            }
        }
        viewModel.getBanner()

        // 初始化事件

        if (savedInstanceState == null) {
            onRefresh()
        }
    }

    private fun onBannerItemClick(data: BannerResponse.Banner, position: Int) {
        val intent = Intent(this.context, WebActivity::class.java).apply {
            putExtra("url", data.url)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun onRefresh() {
        // 刷新 banner 与 articleList
    }
}