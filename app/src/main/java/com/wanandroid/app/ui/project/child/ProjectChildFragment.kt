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
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentProjectChildBinding
import com.wanandroid.app.logic.model.ProjectTitle
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectChildFragment :BaseFragment<FragmentProjectChildBinding>() {

    companion object {
        const val ARG_TITLE = "arg_title"
        const val PROJECT_ID_NEWEST = 0
    }

    private val viewModel: ProjectChildViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_TITLE) }?.apply {
            val project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(ARG_TITLE, ProjectTitle::class.java)
            } else {
                getParcelable(ARG_TITLE)
            }
            Log.d("ProjectChildFragment", "Project created: ${project?.name}")

            // init view
            val projectAdapter = ProjectArticleAdapter(HomeArticleDiffCallback)
            binding.projectRecyclerView.apply {
                adapter = projectAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }

            // init events
            viewLifecycleOwner.lifecycleScope.apply {
                launch {
                    if (project != null) {
                        if (project.id == PROJECT_ID_NEWEST) {
                            viewModel.getNewestProjectFlow.collectLatest { pagingData ->
                                projectAdapter.submitData(pagingData)
                            }
                        } else {
                            viewModel.getProjectListFlow(project.id).collectLatest { pagingData ->
                                projectAdapter.submitData(pagingData)
                            }
                        }
                    }
                }
                launch {
                    // 处理加载状态
                    projectAdapter.loadStateFlow.collect { loadStates ->
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
        }
    }

}