package com.wanandroid.app.ui.navigation.child.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildNavigatorBinding

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
        binding.navChildRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 获取右侧 顶部 分组的索引
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                // 滚动以显示出 分组对应的左侧章节列表
                binding.navChildChapterList.scrollToPosition(firstVisible)
                // 更新左侧章节列表的选中状态
                chapterAdapter.setSelectedIndex(firstVisible)
            }
        })

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
//            val firstVisible = chapterLayoutManager.findFirstVisibleItemPosition()
//            val lastVisible = chapterLayoutManager.findLastVisibleItemPosition()
//            if (index <= firstVisible) {
//                // 从下往上滑直接满足右侧列表对应项滚动到顶的效果
//                binding.navChildRecyclerView.smoothScrollToPosition(index)
//            } else if (index <= lastVisible ) {
//                // 此时列表从上往下滑，对应项默认在底部，需要计算偏移量
//                val offset = binding.navChildRecyclerView.getChildAt(index - firstVisible).top
//                binding.navChildRecyclerView.smoothScrollBy(0, offset)
//            } else {
//                // 如果点击的章节在当前可见范围之外，直接滚动到对应位置
//                binding.navChildRecyclerView.smoothScrollToPosition(index)
//                // 先显示出来目标项，再处理偏移量
//            }
            // 更新选中状态
//            chapterAdapter.setSelectedIndex(index)
        }

        // TODO: 下拉刷新
    }

}