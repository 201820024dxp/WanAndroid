package com.wanandroid.app.ui.navigation.system.child

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemContentBinding
import com.wanandroid.app.logic.model.SystemTopDirectory

class SystemChildContentFragment : BaseFragment<FragmentNavigatorChildSystemContentBinding>() {

    private var stateAdapter: SystemChildContentStateAdapter? = null

    companion object {
        const val NAV_SYS_CONTENT_BUNDLE = "nav_sys_content_bundle"
    }

    private val directory by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.let { bundle ->
            BundleCompat.getParcelable(
                bundle,
                NAV_SYS_CONTENT_BUNDLE,
                SystemTopDirectory::class.java
            )
        } ?: SystemTopDirectory()
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
        Log.d("SystemChildContentFragment", directory.toString())

        // init ViewPager2
        stateAdapter = SystemChildContentStateAdapter(directory.children, this@SystemChildContentFragment)
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
            stateAdapter?.let {
                tab.text = stateAdapter!!.subDirectoryList[position].name
            }
        }.attach()
    }

}