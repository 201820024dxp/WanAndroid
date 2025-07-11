package com.wanandroid.app.ui.project

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentProjectBinding

class ProjectFragment : BaseFragment<FragmentProjectBinding>() {

    private val viewModel : ProjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 初始化viewpager
        val childFragmentAdapter = ProjectChildFragmentAdapter(emptyList(), this)
        binding.projectViewPager.apply {
            isUserInputEnabled = true // 允许用户滑动切换页面
            offscreenPageLimit = 10 // 设置预加载页面数量，避免频繁创建Fragment
            // 设置适配器
            adapter = childFragmentAdapter
        }
        // 绑定TabLayout与ViewPager2
        TabLayoutMediator(binding.projectTabLayout, binding.projectViewPager) { tab, position ->
            // 设置Tab标题
            tab.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(childFragmentAdapter.titleList[position].name, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(childFragmentAdapter.titleList[position].name)
            }
        }.attach()

        // init event
        viewModel.projectTitleList.observe(viewLifecycleOwner) { titles ->
            childFragmentAdapter.titleList = titles
            childFragmentAdapter.notifyDataSetChanged()
        }
    }
}