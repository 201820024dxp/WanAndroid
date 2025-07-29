package com.wanandroid.app.ui.coin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityCoinBinding
import com.wanandroid.app.logic.model.CoinInfo
import com.wanandroid.app.ui.coin.rank.RankActivity
import com.wanandroid.app.ui.web.WebActivity
import kotlinx.coroutines.launch

class CoinActivity : AppCompatActivity() {

    companion object {
        const val URL_COIN_RULE = "https://www.wanandroid.com/blog/show/2653"
    }
    private lateinit var coinHistoryAdapter: CoinListAdapter
    private lateinit var binding: ActivityCoinBinding
    private val viewModel: CoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 设置toolbar
        setSupportActionBar(binding.toolbar)
        initView()
        initEvent()
    }

    private fun initView() {
        // 积分列表
        coinHistoryAdapter = CoinListAdapter(this, CoinListAdapter.CoinHistoryDiffCallback)
        binding.coinRecyclerView.apply {
            adapter = coinHistoryAdapter
            layoutManager = LinearLayoutManager(this@CoinActivity)
            setHasFixedSize(true)
        }
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener { finish() }
        // 积分排行榜点击
        binding.coinRanking.setOnClickListener {
            startActivity(Intent(this, RankActivity::class.java))
        }
        // 积分规则点击
        binding.coinRule.setOnClickListener {
            WebActivity.loadUrl(this, URL_COIN_RULE)
        }
    }

    private fun initEvent() {
        viewModel.selfCoinInfoLiveData.observe(this) { coinInfo ->
            updateSelfCoinInfo(coinInfo)
        }
        lifecycleScope.apply {
            launch {
                viewModel.coinHistoryListFlow.collect { pagingData ->
                    // 提交积分获取历史数据到适配器
                    coinHistoryAdapter.submitData(pagingData)
                }
            }
            launch {
                coinHistoryAdapter.loadStateFlow.collect { loadStates ->
                    // 更新加载状态
                    updateLoadingStates(loadStates)
                }
            }
        }
    }

    private fun updateSelfCoinInfo(coinInfo: CoinInfo) {
        binding.coinTotalCount.text = coinInfo.coinCount.toString()
        binding.coinLevel.text = getString(R.string.level_with_number, coinInfo.level.toString())
        binding.myCoinRank.text = getString(R.string.ranking_with_number, coinInfo.rank)
    }

    private fun updateLoadingStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            // recyclerView是否为加载状态
            val isRefreshing = loadStates.refresh is LoadState.Loading
            val isRefreshed = loadStates.refresh is LoadState.NotLoading
            val isEmptyList = coinHistoryAdapter.itemCount == 0
            // 当item数为0且正在刷新时显示加载进度条，否则隐藏
            loadingProgress.isVisible = isEmptyList && isRefreshing

            // 当item数为0且刷新完成时显示空布局，否则隐藏
            emptyLayout.isVisible = isEmptyList && isRefreshed
        }
    }
}