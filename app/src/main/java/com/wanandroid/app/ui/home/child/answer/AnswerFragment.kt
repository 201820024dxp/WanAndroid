package com.wanandroid.app.ui.home.child.answer

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
import com.wanandroid.app.databinding.FragmentHomeChildAnswerBinding
import com.wanandroid.app.eventbus.FlowBus
import com.wanandroid.app.ui.home.HomeViewModel
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import com.wanandroid.app.widget.RecyclerViewFooterAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AnswerFragment : BaseFragment<FragmentHomeChildAnswerBinding>() {

    companion object {
        const val KEY_CHILD_ANSWER_TAB_PARCELABLE = "key_child_answer_tab_parcelable"
    }

    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var articleAdapter: HomeArticleAdapter

    private val viewModel : AnswerViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeChildAnswerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init view
        articleAdapter = HomeArticleAdapter(this.requireContext(), HomeArticleDiffCallback)
        concatAdapter = articleAdapter.withLoadStateFooter(
            footer = RecyclerViewFooterAdapter(articleAdapter::retry)
        )
        linearLayoutManager = LinearLayoutManager(context)
        binding.answerList.apply {
            layoutManager = linearLayoutManager
            adapter = concatAdapter
            setHasFixedSize(true)
        }

        // init events
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.getAnswerFlow.collect { pagingData ->
                    if (isAdded) {
                        articleAdapter.submitData(pagingData)
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
        }

        // 响应下拉刷新事件
        homeViewModel.onFreshLiveData.observe(viewLifecycleOwner) { tabPosition ->
            if (tabPosition == 2) { // 2表示AnswerFragment
                articleAdapter.refresh()    // 刷新列表
            }
        }
    }

    // TODO: 滚动到顶

}