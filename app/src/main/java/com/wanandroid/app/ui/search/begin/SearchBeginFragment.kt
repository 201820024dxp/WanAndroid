package com.wanandroid.app.ui.search.begin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentSearchBeginBinding
import com.wanandroid.app.ui.search.SearchActivity

class SearchBeginFragment : BaseFragment<FragmentSearchBeginBinding>() {

    private val viewModel:SearchBeginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBeginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init view
        // 搜索热词初始化
        val hotKeyAdapter =
            SearchHotKeyAdapter(this.requireActivity() as SearchActivity, viewModel.hotKeyList)
        binding.hotKeyRecyclerView.apply {
            adapter = hotKeyAdapter
            layoutManager = FlexboxLayoutManager(context)
            setHasFixedSize(true)
        }
        // 搜索历史列表初始化
        val searchHistoryAdapter = SearchHistoryAdapter(this, viewModel.searchHistoryList)
        binding.historyRecyclerView.apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            setHasFixedSize(true)
        }

        // init event
        // 获取搜索热词
        viewModel.searchHotKeyLiveData.observe(viewLifecycleOwner) { hotKeys ->
            Log.d("SearchBeginFragment", "Search hotKeys updated: $hotKeys")
            viewModel.hotKeyList.addAll(hotKeys)
            hotKeyAdapter.notifyDataSetChanged()
        }
        // 搜索历史列表存储与展示
        viewModel.searchHistoryLiveData.observeForever { searchHistory ->
            Log.d("SearchBeginFragment", "Search history updated: $searchHistory")
            searchHistoryAdapter.notifyDataSetChanged()
        }
    }

    // 处理搜索历史点击事件
    fun onSearchHistoryClick(keyword: String) {
        // 发起搜索
        (requireActivity() as SearchActivity).search(keyword)
    }

    // 处理搜索历史删除按钮点击事件
    fun onSearchHistoryDeleteClick(keyword: String) {
        viewModel.removeSearchHistory(keyword)  // 删除搜索记录
    }

}