package com.wanandroid.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.R
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentProfileBinding
import com.wanandroid.app.logic.model.ProfileItemBean
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.ui.coin.CoinActivity
import com.wanandroid.app.ui.collect.CollectActivity
import com.wanandroid.app.ui.share.ShareActivity
import com.wanandroid.app.ui.share.ShareListActivity
import com.wanandroid.app.ui.tools.ToolsActivity
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    companion object {
        const val SHARE_ARTICLE = "分享文章"
        const val MY_SHARE = "我的分享"
        const val MY_COLLECT = "我的收藏"
        const val TOOL_LIST = "工具列表"
    }

    private lateinit var profileItemAdapter: ProfileItemAdapter
    private val viewModel: ProfileViewModel by viewModels()
    private val items = listOf(
        ProfileItemBean(R.drawable.ic_share_48dp, SHARE_ARTICLE, this::shareArticleOnClick),
        ProfileItemBean(R.drawable.ic_article_shortcut_24dp, MY_SHARE, this::myShareOnClick),
        ProfileItemBean(R.drawable.ic_favorite_48dp, MY_COLLECT, this::myCollectOnClick),
        ProfileItemBean(R.drawable.ic_tool_48dp, TOOL_LIST, this::toolsListOnClick)
    )
    private val loginStatusObserver = Observer<Boolean> { isLoggedIn ->
        changeUserInfo(isLoggedIn)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initEvent()
    }

    private fun initView() {
        // 个人信息功能列表
        profileItemAdapter = ProfileItemAdapter(items)
        binding.profileItemList.apply {
            adapter = profileItemAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // TODO: 浮动按钮（设置）点击事件
        binding.settingFabIcon.setOnClickListener {
            Log.d(this.javaClass.simpleName, "you click settings floating button")
        }

        // 个人信息点击事件
        binding.userAvatar.setOnClickListener { // 没有登录时拦截到登录界面
            // 登录状态检查
            AccountManager.checkLogin(requireContext()) {}
        }
        binding.userCoinCount.setOnClickListener {          // 登录后跳转积分界面
            AccountManager.checkLogin(requireContext()) {
                startActivity(Intent(context, CoinActivity::class.java))
            }
        }
    }

    private fun initEvent() {
        // 监听登录状态变化
        viewLifecycleOwner.lifecycleScope.launch {
            AccountManager.isLogin.collect { isLoggedIn ->
                changeUserInfo(isLoggedIn)
            }
        }
        // 更新个人信息
        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            binding.userName.visibility = View.VISIBLE
            binding.userName.text = getString(R.string.profile_userInfo_username,
                userInfo?.userInfo?.username ?: "")
            binding.userId.visibility = View.VISIBLE
            binding.userId.text = getString(R.string.profile_userInfo_userId,
                userInfo?.userInfo?.id.toString())
            binding.userCoinCount.text = getString(
                R.string.coinInfo,
                userInfo?.coinInfo?.coinCount.toString(),
                userInfo?.coinInfo?.level.toString(),
                userInfo?.coinInfo?.rank
            )
        }
    }

    private fun changeUserInfo(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            // 获取个人信息
            viewModel.getUserInfo()
        } else {
            // 个人信息空状态
            binding.userName.visibility = View.GONE
            binding.userId.visibility = View.GONE
            binding.userCoinCount.text = "未登录"
        }
    }

    // 分享文章
    private fun shareArticleOnClick() {
        startActivity(Intent(this.context, ShareActivity::class.java))
    }

    // 我的分享
    private fun myShareOnClick() {
        AccountManager.checkLogin(requireContext()) {
            val intent = Intent(context, ShareListActivity::class.java).apply {
                putExtra(
                    ShareListActivity.KEY_SHARE_LIST_USER_ID,
                    viewModel.userInfo.value?.userInfo?.id.toString()
                )
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP // 设置启动模式为SingleTop
            }
            startActivity(intent)
        }
    }

    // 我的收藏
    private fun myCollectOnClick() {
        AccountManager.checkLogin(requireContext()) {
            startActivity(Intent(context, CollectActivity::class.java))
        }
    }

    // 工具列表
    private fun toolsListOnClick() {
        startActivity(Intent(context, ToolsActivity::class.java))
    }
}