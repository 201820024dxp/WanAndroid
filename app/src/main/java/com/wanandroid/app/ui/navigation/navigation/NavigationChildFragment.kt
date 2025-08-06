package com.wanandroid.app.ui.navigation.navigation

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildNavigatorBinding
import com.wanandroid.app.databinding.ItemNavigatorChildTagLayoutBinding
import com.wanandroid.app.databinding.ItemTextViewChipBinding
import com.wanandroid.app.logic.model.Navigation
import com.wanandroid.app.ui.navigation.widget.NavigationChapterScrollListener
import com.wanandroid.app.ui.web.WebActivity

class NavigationChildFragment : BaseFragment<FragmentNavigatorChildNavigatorBinding>() {

    private lateinit var chapterAdapter: NavigationChapterAdapter
    private lateinit var chapterLayoutManager: LinearLayoutManager
    private lateinit var childAdapter: NavigationChildAdapter
    private lateinit var childLayoutManager: LinearLayoutManager
    private val viewModel: NavigationChildViewModel by viewModels()

    private var selectedCategoryIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavigatorChildNavigatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init views
//        childAdapter = NavigationChildAdapter(emptyList())
//        childLayoutManager = LinearLayoutManager(context)
//        childLayoutManager.initialPrefetchItemCount = 30
//        binding.navChildRecyclerView.apply {        // 初始化 RecyclerView
//            layoutManager = childLayoutManager
//            adapter = childAdapter
//            setHasFixedSize(true)
//            setItemViewCacheSize(30)
//
//            // 新增优化
//            recycledViewPool.setMaxRecycledViews(0, 20)  // 增加视图缓存池大小
//            isNestedScrollingEnabled = false  // 禁用嵌套滚动
//            viewTreeObserver.addOnGlobalLayoutListener {
//                (0 until childAdapter.itemCount).forEach { position ->
//                    itemAnimator?.changeDuration = 0  // 禁用动画
//                }
//            }
//        }
//        chapterAdapter = NavigationChapterAdapter(emptyList())
//        chapterLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.navChildChapterList.apply {
//            adapter = chapterAdapter
//            layoutManager = chapterLayoutManager
//        }
//
//        // init events
//        // Observe navigation list changes
//        viewModel.navigationListLiveData.observe(viewLifecycleOwner) { navigationList ->
//            // 更新适配器数据
//            childAdapter.navigationList = navigationList
//            childAdapter.notifyDataSetChanged()
//            chapterAdapter.navigationList = navigationList
//            chapterAdapter.notifyDataSetChanged()
//            // 隐藏加载动画
//            binding.loadingContainer.loadingProgress.visibility = View.GONE
//        }
//
//        // 右侧章节列表滚动，联动左侧列表高亮
//        binding.navChildRecyclerView.addOnScrollListener(
//            NavigationChapterScrollListener(binding.navChildChapterList, chapterAdapter)
//        )
//
//        // 左侧章节列表点击事件，滚动右侧列表到对应位置
//        chapterAdapter.setOnItemClickListener { index ->
//            // 点击章节列表时，滚动到对应位置
//            val smoothScroller = object : LinearSmoothScroller(context) {
//                override fun getVerticalSnapPreference(): Int {
//                    return SNAP_TO_START // 滚动到顶部
//                }
//                override fun getHorizontalSnapPreference(): Int {
//                    return SNAP_TO_START // 水平滚动不需要
//                }
//            }
//            smoothScroller.targetPosition = index
//            childLayoutManager.startSmoothScroll(smoothScroller)
//            // 更新选中状态
//            chapterAdapter.setSelectedIndex(index)
//        }

        initView()
        initEvent()
        // TODO: 下拉刷新
    }

    private fun initView() {
        // 初始化左侧章节列表
        chapterAdapter = NavigationChapterAdapter(emptyList())
        chapterLayoutManager = LinearLayoutManager(context)
        binding.navChildChapterList.apply {
            adapter = chapterAdapter
            layoutManager = chapterLayoutManager
        }
    }

    private fun initEvent() {
        // 监听数据变化
        viewModel.navigationListLiveData.observe(viewLifecycleOwner) { navigationList ->
            // 更新左侧章节列表
            chapterAdapter.navigationList = navigationList
            chapterAdapter.notifyDataSetChanged()

            // 更新右侧内容
            updateNavigationContent(navigationList)

            // 关闭加载动画
            binding.loadingContainer.loadingProgress.visibility = View.GONE
        }

        // 监听右侧内容视图的滚动
        binding.navChildScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // 计算当前可见的章节索引
            val container = binding.navChildContainer
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child.top <= scrollY && child.bottom >= scrollY) {
                    chapterAdapter.setSelectedIndex(i)
                    // 同步左侧列表位置
                    val leftLayoutManager =
                        binding.navChildChapterList.layoutManager as LinearLayoutManager
                    val recyclerViewHeight = binding.navChildChapterList.height
                    val itemView = leftLayoutManager.findViewByPosition(i)
                    if (itemView != null) {
                        val itemCenter = itemView.top + itemView.height / 2
                        val rvCenter = recyclerViewHeight / 2
                        val scrollBy = itemCenter - rvCenter
                        binding.navChildChapterList.scrollBy(0, scrollBy)
                    } else {
                        binding.navChildChapterList.scrollToPosition(i)
                    }
                    break
                }
            }
        }

        // 左侧章节列表点击事件
        chapterAdapter.setOnItemClickListener { index ->
            // 获取目标视图位置
            val targetView = binding.navChildContainer.getChildAt(index)
            if (targetView != null) {
                binding.navChildScrollView.smoothScrollTo(0, targetView.top)
            }
        }
    }

    private fun updateNavigationContent(navigationList: List<Navigation>) {
        // 检查Fragment是否已附加到Activity
        if (!isAdded) return

        binding.navChildContainer.removeAllViews()
        navigationList.forEach { navigation ->
            // 使用原有的布局文件创建章节视图
            val sectionView = ItemNavigatorChildTagLayoutBinding.inflate(
                layoutInflater,
                binding.navChildContainer,
                false
            )

            // 设置标题
            sectionView.navChildTagTitle.text = navigation.name

            // 添加标签
            navigation.articles.forEach { article ->
                val tagView = ItemTextViewChipBinding.inflate(
                    layoutInflater,
                    sectionView.navChildTagFlexbox,
                    false
                )
                tagView.chipTextView.text = Html.fromHtml(article.title)
                tagView.chipLayout.setOnClickListener {
                    WebActivity.loadUrl(requireContext(), article.link)
                }

                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(12, 12, 12, 12)
                tagView.root.layoutParams = params
                sectionView.navChildTagFlexbox.addView(tagView.root)
            }

            binding.navChildContainer.addView(sectionView.root)
        }
    }
}