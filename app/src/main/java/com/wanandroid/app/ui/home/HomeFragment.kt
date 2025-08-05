package com.wanandroid.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentHomeBinding
import com.wanandroid.app.ui.home.child.answer.AnswerFragment
import com.wanandroid.app.ui.home.child.explore.ExploreFragment
import com.wanandroid.app.ui.home.child.square.SquareFragment
import com.wanandroid.app.ui.search.SearchActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        const val KEY_CHILD_HOME_TAB_PARCELABLE = "key_child_tab_parcelable"
    }

    private val fragmentList = listOf(
        ExploreFragment(),
        SquareFragment(),
        AnswerFragment()
    )

    private lateinit var homeChildFragmentAdapter: HomeChildFragmentAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init view
        // 配置 ViewPager2
        homeChildFragmentAdapter = HomeChildFragmentAdapter(fragmentList, this.childFragmentManager, lifecycle)
        binding.homeViewPager2.adapter = homeChildFragmentAdapter

        // 绑定 TabLayout 与 ViewPager2
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager2) { tab, position ->
            // 设置 Tab 的标题
            tab.text = when (position) {
                0 -> HomeChildFragmentAdapter.HOME_TAB_EXPLORE
                1 -> HomeChildFragmentAdapter.HOME_TAB_SQUARE
                2 -> HomeChildFragmentAdapter.HOME_TAB_ANSWER
                else -> ""
            }
        }.attach()

        // init events
        // 搜索按钮点击事件
        binding.searchIcon.setOnClickListener {
             startActivity(Intent(this.context, SearchActivity::class.java))
        }

        // 定义下拉刷新事件
        binding.homeFragSwipeRefreshLayout.setOnRefreshListener {
            viewModel.onFreshLiveData.value = binding.homeViewPager2.currentItem
            binding.homeFragSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}