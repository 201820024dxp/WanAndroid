package com.wanandroid.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.R
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentProfileBinding
import com.wanandroid.app.logic.model.ProfileItemBean
import com.wanandroid.app.ui.login.LoginActivity

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private lateinit var profileItemAdapter: ProfileItemAdapter
    private val items = listOf(
        ProfileItemBean(R.drawable.ic_notification_48dp, "消息中心"),
        ProfileItemBean(R.drawable.ic_share_48dp, "分享文章"),
        ProfileItemBean(R.drawable.ic_favorite_48dp, "收藏文章"),
        ProfileItemBean(R.drawable.ic_tool_48dp, "工具列表")
    )

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

        // 个人信息初始状态
        binding.userName.visibility = View.GONE
        binding.userId.visibility = View.GONE
        binding.userCoinCount.text = "未登录"

        // 个人信息点击事件
        binding.apply {
            arrayOf(userAvatar, userName, userId, userCoinCount).forEach {
                it.setOnClickListener {
                    // TODO: 登录状态检查
                    Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, LoginActivity::class.java))
                }
            }
        }
    }

    private fun initEvent() {
        // 更新个人信息
    }

}