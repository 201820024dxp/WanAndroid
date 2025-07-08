package com.wanandroid.app.ui.search.begin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentSearchBeginBinding
import com.wanandroid.app.ui.search.SearchActivity

class SearchBeginFragment : BaseFragment<FragmentSearchBeginBinding>() {

    private val viewModel by lazy { SearchBeginViewModel() }

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
        val hotKeyAdapter =
            SearchHotKeyAdapter(this.requireActivity() as SearchActivity, viewModel.hotKeyList)
        binding.hotKeyRecyclerView.apply {
            adapter = hotKeyAdapter
            layoutManager = FlexboxLayoutManager(context)
            setHasFixedSize(true)
        }

        // init event
        viewModel.searchHotKeyLiveData.observe(viewLifecycleOwner) { hotKeys ->
            viewModel.hotKeyList.addAll(hotKeys)
            hotKeyAdapter.notifyDataSetChanged()
        }
    }

}