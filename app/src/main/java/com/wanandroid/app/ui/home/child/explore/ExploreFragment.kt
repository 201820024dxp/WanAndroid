package com.wanandroid.app.ui.home.child.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentHomeChildExploreBinding
import com.wanandroid.app.logic.model.Banner
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import com.wanandroid.app.ui.home.item.HomeBannerAdapter
import com.wanandroid.app.ui.web.WebActivity
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 首页探索页面
 */
class ExploreFragment : BaseFragment<FragmentHomeChildExploreBinding>() {

    companion object {
        const val KEY_CHILD_EXPLORE_TAB_PARCELABLE = "key_child_explore_tab_parcelable"
    }

    private lateinit var bannerAdapter: HomeBannerAdapter

    private lateinit var articleAdapter: HomeArticleAdapter

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
        // 加载article
        articleAdapter = HomeArticleAdapter(HomeArticleDiffCallback)
        binding.exploreList.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = articleAdapter
        }
        // 加载banner
        viewModel.getBanner()

        // 初始化事件
        // 观察banner liveData
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
        // 观察文章列表flow
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.getArticlesFlow.collectLatest{ pagingData ->
                    articleAdapter.submitData(lifecycle, pagingData)
                }
            }
            launch {
                // 处理加载状态
                articleAdapter.loadStateFlow.collect { loadStates ->
                    binding.loadingContainer.apply {
                        // recyclerView是否为加载状态
                        val isRefreshing = loadStates.refresh is LoadState.Loading
                        val isRefreshed = loadStates.refresh is LoadState.NotLoading
                        val isEmptyList = articleAdapter.itemCount == 0
                        // 当item数为0且正在刷新时显示加载进度条，否则隐藏
                        loadingProgress.visibility =
                            if (isEmptyList && isRefreshing) View.VISIBLE else View.GONE

                        // 当item数为0且刷新完成时显示空布局，否则隐藏
                        emptyLayout.visibility =
                            if (isEmptyList && isRefreshed) View.VISIBLE else View.GONE
                    }
                }
            }
        }

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