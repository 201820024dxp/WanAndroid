package com.wanandroid.app.ui.navigation.system.child

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemContentBinding
import com.wanandroid.app.logic.model.SystemTopDirectory

class SystemChildContentFragment : BaseFragment<FragmentNavigatorChildSystemContentBinding>() {

    companion object {
        const val NAV_SYS_CONTENT_BUNDLE = "nav_sys_content_bundle"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildSystemContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 一级目录"体系"下的内容
        arguments?.takeIf { it.containsKey(NAV_SYS_CONTENT_BUNDLE) }?.apply {
            val directory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(NAV_SYS_CONTENT_BUNDLE, SystemTopDirectory::class.java)
            } else {
                getParcelable<SystemTopDirectory>(NAV_SYS_CONTENT_BUNDLE)
            }
            Log.d("SystemChildContentFragment", directory.toString())

            // init ViewPager2
            val stateAdapter = directory?.let {
                SystemChildContentStateAdapter(it.children, this@SystemChildContentFragment)
            }
            binding.navSystemContentViewPager.apply {
                adapter = stateAdapter
                offscreenPageLimit = 10     // 预加载页面
                isUserInputEnabled = false  // 防止滑动冲突，此处不允许用户滑动切换Tab
            }

            // bind ViewPager2 & TabLayout
            TabLayoutMediator(
                binding.navSystemContentTabLayout,
                binding.navSystemContentViewPager
            ) { tab, position ->
                if (stateAdapter != null) {
                    tab.text = stateAdapter.subDirectoryList[position].name
                }
            }.attach()
        }
    }

}