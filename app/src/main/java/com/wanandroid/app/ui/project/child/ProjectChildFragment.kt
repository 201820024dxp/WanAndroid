package com.wanandroid.app.ui.project.child

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.wanandroid.app.databinding.FragmentProjectChildBinding
import com.wanandroid.app.eventbus.FlowBus
import com.wanandroid.app.logic.model.Chapter
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import com.wanandroid.app.ui.project.ProjectViewModel
import com.wanandroid.app.widget.RecyclerViewFooterAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectChildFragment :BaseFragment<FragmentProjectChildBinding>() {

    companion object {
        const val ARG_TITLE = "arg_title"
        const val PROJECT_ID_NEWEST = 0
        const val TAG = "ProjectChildFragment"
    }

    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var projectAdapter: ProjectArticleAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: ProjectChildViewModel by viewModels()
    private val parentViewModel: ProjectViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_TITLE) }?.apply {
            val project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(ARG_TITLE, Chapter::class.java)
            } else {
                @Suppress("DEPRECATION")
                getParcelable(ARG_TITLE)
            }

            if (project == null) {
                Log.e(TAG, "Failed to parse project data")
                return@apply
            }

            Log.d(TAG, "Project created: ${project.name}")

            // init view
            projectAdapter = ProjectArticleAdapter(HomeArticleDiffCallback)
            concatAdapter = projectAdapter.withLoadStateFooter(
                footer = RecyclerViewFooterAdapter(projectAdapter::retry) {
                    projectAdapter.itemCount > 0
                }
            )
            linearLayoutManager = LinearLayoutManager(context)
            binding.projectRecyclerView.apply {
                adapter = concatAdapter
                layoutManager = linearLayoutManager
                setHasFixedSize(true)
            }

            // init events
            viewLifecycleOwner.lifecycleScope.apply {
                // 加载项目文章列表
                launch {
                    if (project.id == PROJECT_ID_NEWEST) {
                        viewModel.getNewestProjectFlow.collectLatest { pagingData ->
                            if (isAdded) {
                                projectAdapter.submitData(pagingData)
                            }
                        }
                    } else {
                        viewModel.getProjectListFlow(project.id).collectLatest { pagingData ->
                            if (isAdded) {
                                projectAdapter.submitData(pagingData)
                            }
                        }
                    }
                }
                launch {
                    // 处理加载状态
                    projectAdapter.loadStateFlow.collect { loadStates ->
                        if (isAdded) {
                            binding.loadingContainer.apply {
                                // recyclerView是否为加载状态
                                val isRefreshing = loadStates.refresh is LoadState.Loading
                                val isRefreshed = loadStates.refresh is LoadState.NotLoading
                                val isEmptyList = projectAdapter.itemCount == 0
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
                        if (isAdded) {  // 添加Fragment生命周期检查
                            for (index in 0..<projectAdapter.itemCount) {
                                val article = projectAdapter.peek(index)
                                if (article != null) {
                                    if (article.id == item.id) {
                                        article.collect = item.collect
                                        projectAdapter.notifyItemChanged(index, article)
                                    }
                                }
                            }
                        }
                    }
                }
                launch {
                    // 监听登录状态的改变
                    AccountManager.isLogin.collect { isLoggedIn ->
                        if (isAdded) {
                            projectAdapter.refresh()
                        }
                    }
                }
            }

            parentViewModel.onProjectRefresh.observe(viewLifecycleOwner) { cid ->
                if (cid != project.id) {
                    return@observe
                }
                else {
                    // 刷新数据
                    Log.d("ProjectChildFragment", "Refreshing project with ID: $cid")
                    projectAdapter.refresh()
                }
            }
        }
    }

}