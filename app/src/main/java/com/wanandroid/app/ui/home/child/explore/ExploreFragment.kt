package com.wanandroid.app.ui.home.child.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentHomeChildExploreBinding
import com.wanandroid.app.eventbus.FlowBus
import com.wanandroid.app.logic.model.Banner
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.ui.home.HomeViewModel
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import com.wanandroid.app.ui.home.item.HomeBannerItemAdapter
import com.wanandroid.app.ui.web.WebActivity
import com.wanandroid.app.widget.RecyclerViewFooterAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 首页探索页面
 */
class ExploreFragment : BaseFragment<FragmentHomeChildExploreBinding>() {

    companion object {
        const val KEY_CHILD_EXPLORE_TAB_PARCELABLE = "key_child_explore_tab_parcelable"
    }

    private lateinit var articleAdapterWithFooter: ConcatAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var homeBannerItemAdapter: HomeBannerItemAdapter
    private lateinit var articleLayoutManager: LinearLayoutManager
    private lateinit var articleAdapter: HomeArticleAdapter

    private val viewModel: ExploreViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })

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
        // 定义 banner 和 article 的 Adapter
        homeBannerItemAdapter = HomeBannerItemAdapter(emptyList(), this)
        articleAdapter = HomeArticleAdapter(this.requireContext(), HomeArticleDiffCallback)
        articleAdapterWithFooter = articleAdapter.withLoadStateFooter(
            footer = RecyclerViewFooterAdapter(articleAdapter::retry)
        )
        // 顺序连接多 Adapter
        concatAdapter = ConcatAdapter(homeBannerItemAdapter, articleAdapterWithFooter)
        articleLayoutManager = LinearLayoutManager(this.context)
        binding.exploreList.apply {
            adapter = concatAdapter
            layoutManager = articleLayoutManager
            setHasFixedSize(true)
        }

        // 初始化事件
        // 观察banner liveData
        viewModel.bannerList.observe(viewLifecycleOwner) { banners ->
            if (banners.isNotEmpty()) {
                homeBannerItemAdapter.banners = banners
                homeBannerItemAdapter.notifyDataSetChanged()
            }
        }
        // 手动获取 Banner 数据
        viewModel.getBanner()
        // 观察文章列表flow
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.getArticlesFlow.collectLatest { pagingData ->
                    if (isAdded) {
                        articleAdapter.submitData(lifecycle, pagingData)
                    }
                }
            }
            launch {
                // 处理加载状态
                articleAdapter.loadStateFlow.collect { loadStates ->
                    if (isAdded) {
                        binding.loadingContainer.apply {
                            // recyclerView是否为加载状态
                            val isRefreshing = loadStates.refresh is LoadState.Loading
                            val isRefreshed = loadStates.refresh is LoadState.NotLoading
                            val isEmptyList = articleAdapter.itemCount == 0
                            // 当item数为0且正在刷新时显示加载进度条，否则隐藏
                            loadingProgress.isVisible = isEmptyList && isRefreshing

                            // 当item数为0且刷新完成时显示空布局，否则隐藏
                            emptyLayout.isVisible = isEmptyList && isRefreshed
                        }
                    }
                }
            }
            launch {
                // 监听收藏状态的改变
                FlowBus.collectStateFlow.collectLatest { item ->
                    for (index in 0..<articleAdapter.itemCount) {
                        val article = articleAdapter.peek(index)
                        if (article != null) {
                            if (article.id == item.id) {
                                article.collect = item.collect
                                articleAdapter.notifyItemChanged(index, article)
                            }
                        }
                    }
                }
            }
            launch {
                // 监听登录状态的改变
                AccountManager.isLogin.collect { isLoggedIn ->
                    if (isAdded) {
                        articleAdapter.refresh()
                    }
                }
            }
        }

        // 响应下拉刷新事件
        homeViewModel.onFreshLiveData.observe(viewLifecycleOwner) { tabPosition ->
            if (tabPosition == 0) { // 0表示ExploreFragment
                onRefresh()
            }
        }
    }

    fun onBannerItemClick(data: Banner, position: Int) {
        WebActivity.loadUrl(this.requireContext(), data.url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    // 刷新事件
    private fun onRefresh() {
        // 刷新 banner 与 articleList
        viewModel.getBanner()
        articleAdapter.refresh()
    }

    // TODO: 滚动到顶事件
}