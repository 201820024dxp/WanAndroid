package com.wanandroid.app.ui.navigation.system.child

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemContentBinding
import com.wanandroid.app.logic.model.SystemTopDirectory
import com.wanandroid.app.ui.navigation.widget.ViewPager2NestedHelper
import kotlin.math.abs

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
    ): View {
        _binding = FragmentNavigatorChildSystemContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 一级目录"体系"下的内容
        Log.d("SystemChildContentFragment", directory.toString())

        // init ViewPager2
        stateAdapter = SystemChildContentStateAdapter(directory.children, this@SystemChildContentFragment)
        binding.navSystemContentViewPager.apply {
            adapter = stateAdapter
            offscreenPageLimit = 10     // 预加载页面
            isUserInputEnabled = true
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

        // 处理TabLayout滑动偶现被外层ViewPager2拦截，导致页面切换的问题
//        binding.root.setOnTouchListener { v, event ->
//            Log.d(this.javaClass.simpleName, "content linear layout touch")
//            false
//        }
//
//        binding.navSystemContentTabLayout.setOnTouchListener(object : View.OnTouchListener {
//            var startX = 0f
//            var startY = 0f
//
//            override fun onTouch(v: View, event: MotionEvent): Boolean {
//                Log.d(this@SystemChildContentFragment.javaClass.simpleName, "tabLayout touch")
//                when(event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        startX = event.x
//                        startY = event.y
//                        v.parent.requestDisallowInterceptTouchEvent(true)
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        val dx = event.x - startX
//                        val dy = event.y - startY
//                        if (abs(dx) > abs(dy)) {
//                            v.parent.requestDisallowInterceptTouchEvent(true)
//                        }
//                    }
//                }
//                return false
//            }
//        })

        // 解决ViewPager2的嵌套滑动冲突
//        ViewPager2NestedHelper.bindNestedScroll(
//            binding.navSystemContentViewPager
//        )
    }

}