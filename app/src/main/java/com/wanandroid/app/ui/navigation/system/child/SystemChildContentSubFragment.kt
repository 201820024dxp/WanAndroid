package com.wanandroid.app.ui.navigation.system.child

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemContentSubBinding
import com.wanandroid.app.eventbus.FlowBus
import com.wanandroid.app.logic.model.SystemSubDirectory
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import com.wanandroid.app.widget.RecyclerViewFooterAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SystemChildContentSubFragment :
    BaseFragment<FragmentNavigatorChildSystemContentSubBinding>() {

    companion object {
        const val NAV_SYS_CONTENT_SUB_BUNDLE = "nav_sys_content_sub_bundle"
    }

    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var articleAdapter: HomeArticleAdapter
    val viewModel: SystemChildContentSubViewModel by viewModels()

    private val subDirectory by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.let {
            BundleCompat.getParcelable(
                it,
                NAV_SYS_CONTENT_SUB_BUNDLE,
                SystemSubDirectory::class.java
            )
        }
            ?: SystemSubDirectory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildSystemContentSubBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init view
        articleAdapter = HomeArticleAdapter(requireContext(), HomeArticleDiffCallback)
        concatAdapter = articleAdapter.withLoadStateFooter(
            footer = RecyclerViewFooterAdapter(articleAdapter::retry)
        )
        linearLayoutManager = LinearLayoutManager(context)
        binding.systemSubContentRecyclerView.apply {
            adapter = concatAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }

        // init event
        Log.d("SystemChildContentSubFragment", "subDirectory: $subDirectory")
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.getSystemArticleList(subDirectory.id)
                    .collectLatest { pagingData ->
                        Log.d("SystemChildContentSubFragment", "cid: ${subDirectory.id}")
                        articleAdapter.submitData(pagingData)
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
    }

}