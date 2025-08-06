package com.wanandroid.app.ui.group.child

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentGroupChildBinding
import com.wanandroid.app.eventbus.FlowBus
import com.wanandroid.app.logic.model.Chapter
import com.wanandroid.app.ui.group.GroupViewModel
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import com.wanandroid.app.widget.RecyclerViewFooterAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GroupChildFragment : BaseFragment<FragmentGroupChildBinding>() {

    companion object {
        const val GROUP_CHILD_CHAPTER_BUNDLE = "group_child_chapter_bundle"
    }

    private lateinit var concatAdapter: ConcatAdapter
    private val viewModel: GroupChildViewModel by viewModels()
    private val groupViewModel: GroupViewModel by viewModels(ownerProducer = {requireParentFragment()})

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var articleAdapter: HomeArticleAdapter

    private val chapterBundle by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.let { bundle ->
            BundleCompat.getParcelable(bundle, GROUP_CHILD_CHAPTER_BUNDLE, Chapter::class.java)
        } ?: Chapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("GroupChildFragment", chapterBundle.name)
        initView()
        initEvent()
    }

    private fun initView() {
        articleAdapter = HomeArticleAdapter(this.requireContext(), HomeArticleDiffCallback)
        concatAdapter = articleAdapter.withLoadStateFooter(
            footer = RecyclerViewFooterAdapter(articleAdapter::retry)
        )
        linearLayoutManager = LinearLayoutManager(context)
        binding.groupChildRecyclerView.apply {
            adapter = concatAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun initEvent() {
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.getGroupArticleList(chapterBundle.id).collectLatest { pageData ->
                    if (isAdded) {
                        articleAdapter.submitData(pageData)
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

        // 处理刷新事件
        groupViewModel.onGroupRefresh.observe(viewLifecycleOwner) { id ->
            if (id != chapterBundle.id) {
                return@observe
            }
            else {
                // 刷新数据
                Log.d("GroupChildFragment", "Refreshing group with ID: ${id}")
                articleAdapter.refresh()
            }
        }
    }

}