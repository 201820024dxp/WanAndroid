package com.wanandroid.app.ui.collect

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.databinding.ActivityCollectBinding
import com.wanandroid.app.widget.RecyclerViewFooterAdapter
import kotlinx.coroutines.launch

class CollectActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var collectListAdapter: CollectListAdapter
    private lateinit var binding: ActivityCollectBinding

    private val viewModel : CollectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCollectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        initView()
        initEvent()
    }

    private fun initView() {
        // 返回
        binding.toolbar.setNavigationOnClickListener { finish() }
        // 下拉刷新
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            collectListAdapter.refresh()    // 重新加载数据
        }
        // 加载列表
        collectListAdapter = CollectListAdapter(CollectListDiffCallback)
        concatAdapter = collectListAdapter.withLoadStateFooter(
            RecyclerViewFooterAdapter(collectListAdapter::retry)
        )
        linearLayoutManager = LinearLayoutManager(this)
        binding.collectedArticlesRecyclerView.apply {
            adapter = concatAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun initEvent() {
        lifecycleScope.apply {
            launch {
                viewModel.collectListFlow.collect {
                    collectListAdapter.submitData(it)
                }
            }
            launch {
                collectListAdapter.loadStateFlow.collect { states ->
                    updateLoadingStates(states)
                }
            }
        }
    }


    private fun updateLoadingStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            // recyclerView是否为加载状态
            val isRefreshing = loadStates.refresh is LoadState.Loading
            val isRefreshed = loadStates.refresh is LoadState.NotLoading
            val isEmptyList = collectListAdapter.itemCount == 0
            // 当item数为0且正在刷新时显示加载进度条，否则隐藏
            loadingProgress.isVisible = isEmptyList && isRefreshing
            // 当item数为0且刷新完成时显示空布局，否则隐藏
            emptyLayout.isVisible = isEmptyList && isRefreshed
        }
    }
}