package com.wanandroid.app.ui.navigator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorBinding

class NavigatorFragment : BaseFragment<FragmentNavigatorBinding>() {

    companion object {
        const val TAB_CHILD_NAVIGATOR = "导航"
        const val TAB_CHILD_SYSTEM = "体系"
        const val TAB_CHILD_COURSE = "教程"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init view
        val tabList = listOf(TAB_CHILD_NAVIGATOR, TAB_CHILD_SYSTEM, TAB_CHILD_COURSE)
        val stateAdapter = NavigatorFragmentStateAdapter(tabList, this)
        binding.navigatorViewPager2.apply {
            isUserInputEnabled = true // 允许用户滑动切换页面
            offscreenPageLimit = 10 // 设置预加载页面数量，避免频繁创建Fragment
            // 设置适配器
            adapter = stateAdapter
        }
        TabLayoutMediator(binding.navigatorTabLayout, binding.navigatorViewPager2) { tab, position ->
            // 设置Tab标题
            tab.text = tabList[position]
        }.attach()

        // init events
        // TODO: 下拉刷新
        binding.navigatorSwipeRefresh.setOnRefreshListener { // 刷新操作
            // 这里可以添加刷新逻辑
            binding.navigatorSwipeRefresh.isRefreshing = false
        }
        // TODO: 滚动到顶
    }
}