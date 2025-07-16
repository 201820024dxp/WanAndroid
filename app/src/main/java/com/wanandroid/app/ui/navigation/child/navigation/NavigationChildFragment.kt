package com.wanandroid.app.ui.navigation.child.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildNavigatorBinding
import com.wanandroid.app.ui.navigation.widget.NavigationChapterScrollListener

class NavigationChildFragment : BaseFragment<FragmentNavigatorChildNavigatorBinding>() {

    private val viewModel: NavigationChildViewModel by viewModels()

    private var selectedCategoryIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildNavigatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init views
        val childAdapter = NavigationChildAdapter(emptyList())
        val childLayoutManager = LinearLayoutManager(context)
        binding.navChildRecyclerView.apply {        // 初始化 RecyclerView
            layoutManager = childLayoutManager
            adapter = childAdapter
            setHasFixedSize(true)
        }
        val chapterAdapter = NavigationChapterAdapter(emptyList())
        val chapterLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.navChildChapterList.apply {
            adapter = chapterAdapter
            layoutManager = chapterLayoutManager
        }

        // init events
        // Observe navigation list changes
        viewModel.navigationListLiveData.observe(viewLifecycleOwner) { navigationList ->
            // 更新适配器数据
            childAdapter.navigationList = navigationList
            childAdapter.notifyDataSetChanged()
            chapterAdapter.navigationList = navigationList
            chapterAdapter.notifyDataSetChanged()
            // 隐藏加载动画
            binding.loadingContainer.loadingProgress.visibility = View.GONE
        }

        // 右侧章节列表滚动，联动左侧列表高亮
        binding.navChildRecyclerView.addOnScrollListener(
            NavigationChapterScrollListener(binding.navChildChapterList, chapterAdapter)
        )

        // 左侧章节列表点击事件，滚动右侧列表到对应位置
        chapterAdapter.setOnItemClickListener { index ->
            // 点击章节列表时，滚动到对应位置
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START // 滚动到顶部
                }
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START // 水平滚动不需要
                }
            }
            smoothScroller.targetPosition = index
            childLayoutManager.startSmoothScroll(smoothScroller)
            // 更新选中状态
            chapterAdapter.setSelectedIndex(index)
        }

        // TODO: 下拉刷新
    }

}