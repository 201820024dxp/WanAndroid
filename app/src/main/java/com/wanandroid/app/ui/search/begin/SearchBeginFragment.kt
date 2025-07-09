package com.wanandroid.app.ui.search.begin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentSearchBeginBinding
import com.wanandroid.app.ui.search.SearchActivity

class SearchBeginFragment : BaseFragment<FragmentSearchBeginBinding>() {

    private val viewModel:SearchBeginViewModel by viewModels()

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
        // TODO：搜索历史列表

        // init event
        // 获取搜索热词
        viewModel.searchHotKeyLiveData.observe(viewLifecycleOwner) { hotKeys ->
            viewModel.hotKeyList.addAll(hotKeys)
            hotKeyAdapter.notifyDataSetChanged()
        }
        // TODO：搜索历史列表存储与展示
    }

}