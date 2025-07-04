package com.wanandroid.app.ui.home.child.answer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentHomeChildAnswerBinding
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import kotlinx.coroutines.launch

class AnswerFragment : BaseFragment<FragmentHomeChildAnswerBinding>() {

    companion object {
        const val KEY_CHILD_ANSWER_TAB_PARCELABLE = "key_child_answer_tab_parcelable"
    }

    private lateinit var articleAdapter: HomeArticleAdapter

    private val viewModel by lazy { AnswerViewModel() }

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
        binding.answerList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
            setHasFixedSize(true)
        }

        // init events
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.getAnswerFlow.collect { pagingData ->
                    articleAdapter.submitData(pagingData)
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
                        loadingProgress.isVisible = isEmptyList && isRefreshing

                        // 当item数为0且刷新完成时显示空布局，否则隐藏
                        emptyLayout.isVisible = isEmptyList && isRefreshed
                    }
                }
            }
        }

    }

    // TODO: 下拉刷新

    // TODO: 滚动到顶

}