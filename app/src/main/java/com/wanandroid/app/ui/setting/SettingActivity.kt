package com.wanandroid.app.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivitySettingBinding
import com.wanandroid.app.logic.model.SettingItemBean
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.utils.showShortToast
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {

    val TAG = "SettingActivity"

    private lateinit var binding: ActivitySettingBinding
    private val settingItems = listOf(
        SettingItemBean(
            R.drawable.ic_dark_mode_32dp_color_primary, "深色模式",
            "跟随系统", ::darkModeSwitchOnClick
        ),
        SettingItemBean(
            R.drawable.ic_wanandroid_color_primary, "关于",
            "", ::aboutAppOnClick
        ),
        SettingItemBean(
            R.drawable.ic_code_32dp_color_primary, "源代码",
            "https://github.com/201820024dxp/WanAndroid", ::sourceCodeOnClick
        ),
    )

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        initView()
        initEvent()
    }

    private fun initView() {
        // 返回按钮
        binding.toolbar.setNavigationOnClickListener { finish() }
        // getPackageManager方法需要上下文Context，因此不能在类中直接作为属性进行初始化
        settingItems[1].desc = "版本号: ${getVersion()}"
        // 初始化设置item
        arrayOf(
            binding.darkModeSwitch,
            binding.aboutApp,
            binding.sourceCode
        ).forEachIndexed { index, itemBinding ->
            itemBinding.toolIcon.setImageResource(settingItems[index].iconResource)
            itemBinding.toolName.text = settingItems[index].title
            itemBinding.toolDesc.text = settingItems[index].desc
            itemBinding.root.setOnClickListener {
                settingItems[index].onclick()
            }
        }
        // 初始化退出登录按钮
        binding.logout.setOnClickListener { logoutOnClick() }
    }

    private fun initEvent() {
        // 监听登出按钮可用状态
        lifecycleScope.launch {
            AccountManager.isLogin.collect { isLoggedIn ->
                binding.logout.isEnabled = isLoggedIn
                binding.logout.isClickable = isLoggedIn
            }
        }
        // 监听登出结果
        viewModel.logoutLiveData.observe(this) { result ->
            when(result.errorCode) {
                0 -> {
                    Log.d(TAG, "Logout successful")
                    "退出成功".showShortToast()
                    AccountManager.setLoginStatus(false)    // 设置全局登录状态为未登录
                    finish() // 退出登录后关闭设置界面
                }
                else -> {
                    Log.e(TAG, "Logout failed: ${result.errorMsg}")
                    result.errorMsg.showShortToast()    // 显示错误信息
                }
            }
        }
    }

    private fun darkModeSwitchOnClick() {
        Log.d(TAG, "Dark mode switch clicked")
    }

    private fun aboutAppOnClick() {}
    private fun sourceCodeOnClick() {}

    private fun logoutOnClick() {
        viewModel.logout()
    }

    private fun getVersion(): String {
        return packageManager.getPackageInfo(packageName, 0).versionName.toString()
    }
}