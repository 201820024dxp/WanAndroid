package com.wanandroid.app.ui.navigation.course.child

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityCourseListBinding
import com.wanandroid.app.logic.model.ProjectTitle
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CourseListActivity : AppCompatActivity() {

    companion object {
        const val KEY_COURSE_LIST_BUNDLE = "key_course_list_bundle"
    }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var articleAdapter: HomeArticleAdapter
    private lateinit var binding: ActivityCourseListBinding

    private val viewModel : CourseListViewModel by viewModels()

    private val intentData by lazy {
        // 传入的是教程基本信息（名称、id等），与ProjectTitle数据格式一致，复用ProjectTitle
        IntentCompat.getParcelableExtra(intent, KEY_COURSE_LIST_BUNDLE, ProjectTitle::class.java)
            ?: ProjectTitle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCourseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.courseListMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 设置自定义toolbar
        setSupportActionBar(binding.courseToolbar)
        initView()
        initEvents()
    }

    private fun initView() {
        // 设置标题
        binding.courseTitle.text = intentData.name.split("_")[0]
        // 标题返回按钮点击事件
        binding.courseToolbar.setNavigationOnClickListener { finish() }
        // 设置教程文章列表
        articleAdapter = HomeArticleAdapter(this, HomeArticleDiffCallback)
        linearLayoutManager = LinearLayoutManager(this)
        binding.courseList.apply {
            adapter = articleAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun initEvents() {
        lifecycleScope.apply{
            // 监听文章列表变化
            launch{
                viewModel.getCourseListById(intentData.id).collectLatest { pageData ->
                    articleAdapter.submitData(pageData)
                }
            }
            launch { // 处理加载状态
                articleAdapter.loadStateFlow.collect { loadStates ->
                    binding.loadingContainer.apply {
                        // recyclerView是否为加载状态
                        val isRefreshing = loadStates.refresh is LoadState.Loading
                        val isRefreshed = loadStates.refresh is LoadState.NotLoading
                        val isEmptyList = articleAdapter.itemCount == 0
                        // 当item数为0且正在刷新时显示加载进度条，否则隐藏
                        loadingProgress.isVisible = isEmptyList && isRefreshing
                        // 当item数为0且刷新完成时显示空布局，否则隐藏
                        emptyLayout.isVisible = isEmptyList && isRefreshed
                    }
                }
            }
        }
    }
}