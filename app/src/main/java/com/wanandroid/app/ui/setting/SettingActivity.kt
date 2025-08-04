package com.wanandroid.app.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.wanandroid.app.R
import com.wanandroid.app.app.AppPreferences
import com.wanandroid.app.databinding.ActivitySettingBinding
import com.wanandroid.app.logic.model.SettingItemBean
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.ui.setting.about.AboutActivity
import com.wanandroid.app.ui.web.WebActivity
import com.wanandroid.app.utils.showShortToast
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {

    val TAG = "SettingActivity"

    private lateinit var binding: ActivitySettingBinding
    private val curMode = when (AppCompatDelegate.getDefaultNightMode()) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> "跟随系统"
        AppCompatDelegate.MODE_NIGHT_NO -> "普通模式"
        AppCompatDelegate.MODE_NIGHT_YES -> "深色模式"
        else -> "跟随系统"
    }
    private val settingItems = listOf(
        SettingItemBean(
            R.drawable.ic_dark_mode_32dp_color_primary, "深色模式",
            curMode, ::darkModeSwitchOnClick
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
            when (result.errorCode) {
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

        // 创建PopupMenu
        val popupMenu = PopupMenu(this, binding.darkModeSwitch.toolName)
        // 加载菜单资源
        val inflater = menuInflater
        inflater.inflate(R.menu.dark_mode_menu, popupMenu.menu)

        // 设置菜单项点击事件
        popupMenu.setOnMenuItemClickListener { item ->
            val selectedMode = when (item.itemId) {
                R.id.followSystem -> {
                    binding.darkModeSwitch.toolDesc.text = "跟随系统"
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }

                R.id.ordinaryMode -> {
                    binding.darkModeSwitch.toolDesc.text = "普通模式"
                    AppCompatDelegate.MODE_NIGHT_NO
                }

                R.id.darkMode -> {
                    binding.darkModeSwitch.toolDesc.text = "深色模式"
                    AppCompatDelegate.MODE_NIGHT_YES
                }

                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            // 保存用户选择的主题
            AppPreferences.saveThemeMode(selectedMode)
            // 更新主题
            AppCompatDelegate.setDefaultNightMode(selectedMode)
            true
        }

        // 显示PopupMenu
        popupMenu.show()
    }

    private fun aboutAppOnClick() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    private fun sourceCodeOnClick() {
        WebActivity.loadUrl(this, "https://github.com/201820024dxp/WanAndroid")
    }

    private fun logoutOnClick() {
        viewModel.logout()
    }

    private fun getVersion(): String {
        return packageManager.getPackageInfo(packageName, 0).versionName.toString()
    }
}