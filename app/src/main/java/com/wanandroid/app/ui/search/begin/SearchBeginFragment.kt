package com.wanandroid.app.ui.search.begin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentSearchBeginBinding
import com.wanandroid.app.ui.search.SearchActivity
import kotlinx.coroutines.launch

class SearchBeginFragment : BaseFragment<FragmentSearchBeginBinding>() {

    private lateinit var hotKeyAdapter: SearchHotKeyAdapter
    private lateinit var flexboxLayoutManager: FlexboxLayoutManager
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
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
        hotKeyAdapter =
            SearchHotKeyAdapter(this.requireActivity() as SearchActivity, viewModel.hotKeyList)
        flexboxLayoutManager = FlexboxLayoutManager(context)
        binding.hotKeyRecyclerView.apply {
            adapter = hotKeyAdapter
            layoutManager = flexboxLayoutManager
            setHasFixedSize(true)
        }
        // 搜索历史列表初始化
        searchHistoryAdapter = SearchHistoryAdapter(this, viewModel.searchHistoryList)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        binding.historyRecyclerView.apply {
            adapter = searchHistoryAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }

        // init event
        // 获取搜索热词
        viewModel.searchHotKeyLiveData.observe(viewLifecycleOwner) { hotKeys ->
            Log.d("SearchBeginFragment", "Search hotKeys updated: $hotKeys")
            viewModel.hotKeyList.addAll(hotKeys)
            hotKeyAdapter.notifyDataSetChanged()
        }
        // 搜索历史列表改变监听
        viewModel.searchHistoryLiveData.observe(viewLifecycleOwner) { searchHistory ->
            Log.d("SearchBeginFragment", "Search history updated: $searchHistory")
            searchHistoryAdapter.notifyDataSetChanged()
        }
        // 搜索历史列表初始化
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchHistoryFlow.collect {
                if (isAdded) {
                    searchHistoryAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    // 处理搜索历史点击事件
    fun onSearchHistoryClick(keyword: String) {
        val activity = requireActivity() as SearchActivity
        // 发起搜索
        activity.setSearchEditText(keyword)
        activity.search(keyword)
    }

    // 处理搜索历史删除按钮点击事件
    fun onSearchHistoryDeleteClick(keyword: String) {
        viewModel.removeSearchHistory(keyword)  // 删除搜索记录
    }

}