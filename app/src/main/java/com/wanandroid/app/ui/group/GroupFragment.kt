package com.wanandroid.app.ui.group

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentGroupBinding

class GroupFragment : BaseFragment<FragmentGroupBinding>() {

    private lateinit var groupFragmentStateAdapter: GroupFragmentStateAdapter
    private val viewModel: GroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initEvent()
    }

    private fun initView() {
        groupFragmentStateAdapter = GroupFragmentStateAdapter(emptyList(), this)
        binding.groupViewPager.apply {
            adapter = groupFragmentStateAdapter
            offscreenPageLimit = 10     // 缓存10个页面
            isUserInputEnabled = true
        }
        TabLayoutMediator(binding.groupTabLayout, binding.groupViewPager) { tab, position ->
            // 设置Tab标题
            tab.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(groupFragmentStateAdapter.groupChapterList[position].name,
                    Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(groupFragmentStateAdapter.groupChapterList[position].name)
            }
        }.attach()
        binding.groupSwipeRefreshLayout.setOnRefreshListener {
            // TODO: 刷新操作
            binding.groupSwipeRefreshLayout.isRefreshing = false // 停止刷新动画
        }
    }

    private fun initEvent() {
        // 监听chapter数据
        viewModel.groupChapterList.observe(viewLifecycleOwner) { chapterList ->
            groupFragmentStateAdapter.groupChapterList = chapterList
            groupFragmentStateAdapter.notifyDataSetChanged()
        }
    }
}