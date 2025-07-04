package com.wanandroid.app.ui.share

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityShareListBinding
import com.wanandroid.app.logic.model.ShareResponse
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 分享人(自己 和 其他用户)对应分享列表页面
 */
class ShareListActivity : AppCompatActivity() {

    companion object {
        const val KEY_SHARE_LIST_USER_ID = "key_share_list_user_id"
    }

    private lateinit var articleAdapter: HomeArticleAdapter
    private lateinit var binding: ActivityShareListBinding

    private val userId by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(KEY_SHARE_LIST_USER_ID) ?: ""
    }

    private val viewModel: ShareListViewModel by lazy { ShareListViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShareListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // init view
        articleAdapter = HomeArticleAdapter(this, HomeArticleDiffCallback)
        binding.shareList.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
            adapter = articleAdapter
            setHasFixedSize(true)
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        // init events
        lifecycleScope.apply {
            launch {
                // 获取分享人数据并填充
                viewModel.shareResponseFlow.collectLatest { shareResponse ->
                    val coinInfo = shareResponse.coinInfo
                    binding.userName.text = "用户名: ${ coinInfo.nickname }"
                    binding.userShareCount.text = "分享了${shareResponse.shareArticles.total}篇文章"
                    binding.userCoinCount.text =
                        "积分: ${coinInfo.coinCount}  等级: ${coinInfo.level}  排名: ${coinInfo.rank}"
                }
            }
            launch {
                // 获取分享文章列表
                viewModel.shareListFlow.collectLatest { pagingData ->
                    articleAdapter.submitData(lifecycle, pagingData)
                }
            }
            launch {
                // 处理加载状态
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

        // fetch data
        viewModel.fetch(userId)

    }
}