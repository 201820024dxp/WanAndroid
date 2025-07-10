package com.wanandroid.app.ui.search.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentSearchResultBinding
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import com.wanandroid.app.ui.search.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>() {

    private lateinit var articleAdapter: HomeArticleAdapter

    val searchViewModel by activityViewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init view
        articleAdapter = HomeArticleAdapter(requireContext(), HomeArticleDiffCallback)
        binding.searchResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
            setHasFixedSize(true)
        }

        // init event
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                searchViewModel.searchResponseFlow.collect { pagingData ->
                    // 提交数据到适配器
                    articleAdapter.submitData(lifecycle, pagingData)
                }
            }
            launch { // 处理加载状态
                articleAdapter.loadStateFlow.collect { loadStates ->
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
    }

}