package com.wanandroid.app.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityMainBinding
import com.wanandroid.app.ui.group.GroupFragment
import com.wanandroid.app.ui.home.HomeFragment
import com.wanandroid.app.ui.navigator.NavigatorFragment
import com.wanandroid.app.ui.profile.ProfileFragment
import com.wanandroid.app.ui.project.ProjectFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_CURRENT_FRAGMENT_INDEX = "key_current_fragment_index"
    }

    // Fragment 列表
    private val fragmentList = listOf(
        HomeFragment(),
        ProjectFragment(),
        NavigatorFragment(),
        GroupFragment(),
        ProfileFragment()
    )

    // 当前显示的 Fragment 索引
    private var currentFragmentIndex = -1

    val mainViewModel by lazy { MainViewModel() }

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // 设置沉浸式导航栏
        mainViewModel.bottomNavigatorViewHeight = mainBinding.navView.layoutParams.height
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.navView) { v, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updateLayoutParams {
                // 为了设置沉浸式底部导航栏效果，需要获取 自定义导航栏高度 和 系统导航栏高度
                // 为了避免 自定义导航栏 每次点击都会重新计算高度，从而累加高度，将自定义导航栏高度保存到ViewModel中
                height = mainViewModel.bottomNavigatorViewHeight + navBars.bottom
            }
            v.setPadding(0, 0, 0, navBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        // 设置三按钮式导航栏为透明色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        if (savedInstanceState == null) {
            switchFragment(0)
        }

        // 设置底部导航栏点击事件
        mainBinding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_item_home -> switchFragment(0)
                R.id.nav_item_project -> switchFragment(1)
                R.id.nav_item_navigator -> switchFragment(2)
                R.id.nav_item_group -> switchFragment(3)
                R.id.nav_item_profile -> switchFragment(4)
                else -> switchFragment(0)
            }
            true
        }

        // TODO: 设置“个人信息”图标右上角的 未读消息 提醒
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // 保存当前 Fragment 的索引
        outState.putInt(KEY_CURRENT_FRAGMENT_INDEX, currentFragmentIndex)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复当前 Fragment 的索引
        currentFragmentIndex = savedInstanceState.getInt(KEY_CURRENT_FRAGMENT_INDEX, 0)
        switchFragment(currentFragmentIndex)
    }

    private fun switchFragment(index: Int) {
        if (index == currentFragmentIndex) return
        supportFragmentManager.beginTransaction().apply {
            // 隐藏当前 Fragment
            if (currentFragmentIndex >= 0 && currentFragmentIndex < fragmentList.size) {
                hide(fragmentList[currentFragmentIndex])
            }
            // 显示新的 Fragment
            if (index >= 0 && index < fragmentList.size) {
                val fragment = fragmentList[index]
                // 如果 Fragment 还没有添加到 Activity 中，则添加并展示
                if (!fragment.isAdded) {
                    add(R.id.nav_host_fragment_activity_main, fragment, fragment.javaClass.simpleName)
                    show(fragment)
                } else {
                    show(fragment)
                }
            }
            currentFragmentIndex = index
            commit()
        }
    }
}