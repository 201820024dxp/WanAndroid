package com.wanandroid.app.ui.setting.about

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 设置工具栏
        setSupportActionBar(binding.aboutToolbar)
        binding.aboutToolbar.setNavigationOnClickListener { finish() }

        // 设置版本号
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        binding.aboutVersionName.text = getString(R.string.version_name, versionName)

        // 设置关于软件内容
        val aboutContent = Html.fromHtml("<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <h1>应用介绍</h1>\n" +
                "    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "WanAndroid应用是基于WanAndroid网站的开放API实现的Android客户端，" +
                "致力于为用户提供方便、快捷的Android信息浏览服务。</p>\n" +
                "\n<br/>" +
                "    <h1>版本更新记录</h1>\n" +
                "    \n" +
                "    <h2 class=\"version-header\">v1.0.1 (2025/8/4)</h2>\n" +
                "    <h4>应用</h4>\n" +
                "    <ul>\n" +
                "        <li>新增 支持收藏站内文章功能。</li>\n" +
                "        <li>新增 实现夜间模式与普通模式之间的切换功能。</li>\n" +
                "        <li>新增 图片加载时显示占位图，提升用户体验。</li>\n" +
                "        <li>修复 解决应用重启后自定义UI模式丢失的问题。</li>\n" +
                "        <li>修复 修复部分情况下页面重叠的显示问题。</li>\n" +
                "        <li>优化 优化收藏状态的同步机制，确保各页面收藏状态一致。</li>\n" +
                "        <li>优化 在列表页添加页脚，改善翻页体验。</li>\n" +
                "        <li>优化 内容浏览页弹窗拦截，改善内容浏览体验。</li>\n" +
                "        <li>优化 常用网站导航的滚动效果，缓解滑动掉帧问题。</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <h4>首页</h4>\n" +
                "    <ul>\n" +
                "        <li>修复 解决首页Banner滑动时的冲突问题。</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <h4>个人账号</h4>\n" +
                "    <ul>\n" +
                "        <li>新增 实现登录、注册、退出功能，并完善个人账号界面展示。</li>\n" +
                "        <li>新增 展示个人积分历史、积分排行榜以及积分规则功能。</li>\n" +
                "        <li>新增 增加文章分享功能，支持将文章分享到广场。</li>\n" +
                "        <li>新增 我的分享功能，允许用户查看和管理自己分享的文章。</li>\n" +
                "        <li>新增 我的收藏功能，支持查看和管理已收藏的站内文章。</li>\n" +
                "        <li>新增 工具列表功能，提供便捷跳转至常用开发工具页面。</li>\n" +
                "        <li>修复 优化登录与注册界面的逻辑，解决部分UI展示问题。</li>\n" +
                "        <li>修复 解决登录状态监听问题，确保用户登录状态正确显示。</li>\n" +
                "    </ul>\n" +
                "\n<br/>" +
                "    <h2 class=\"version-header\">v1.0.0 (2025/7/22)</h2>\n" +
                "    \n" +
                "    <h4>首页</h4>\n" +
                "    <ul>\n" +
                "        <li>新增 首页Banner、置顶文章及首页文章列表的加载与展示功能。</li>\n" +
                "        <li>新增 文章详情内容展示功能，支持文章分享。</li>\n" +
                "        <li>新增 广场文章列表的加载与展示功能。</li>\n" +
                "        <li>新增 问答文章列表的加载与展示功能。</li>\n" +
                "        <li>新增 提供查看文章分享历史的功能，展示文章的分享者及分享历史。</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <h4>搜索</h4>\n" +
                "    <ul>\n" +
                "        <li>新增 支持站内文章搜索功能，提升内容查找效率。</li>\n" +
                "        <li>新增 增加历史搜索记录与搜索热词展示功能，并优化搜索热词的瀑布流展示效果。</li>\n" +
                "        <li>修复 修正点击搜索历史时未更新文本框内容的问题。</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <h4>项目</h4>\n" +
                "    <ul>\n" +
                "        <li>新增 支持按分类展示不同类型的项目文章，便于用户筛选。</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <h4>导航</h4>\n" +
                "    <ul>\n" +
                "        <li>新增 相关网站导航的瀑布流展示功能，便于快速浏览。</li>\n" +
                "        <li>新增 支持按知识体系查看站内文章，提供更精准的内容筛选。</li>\n" +
                "        <li>优化 优化导航栏目的展示界面，支持两级导航及联动效果，提升用户体验。</li>\n" +
                "        <li>新增 教程栏目及其对应文章的展示功能，丰富教学内容。</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <h4>公众号</h4>\n" +
                "    <ul>\n" +
                "        <li>新增 支持展示各公众号发布的文章，方便用户浏览订阅内容。</li>\n" +
                "    </ul>\n" +
                "\n" +
                "</body>\n" +
                "</html>")
        binding.aboutText.text = aboutContent
    }
}