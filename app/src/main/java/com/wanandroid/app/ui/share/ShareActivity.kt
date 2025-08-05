package com.wanandroid.app.ui.share

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.wanandroid.app.databinding.ActivityShareBinding
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.utils.showShortToast

class ShareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareBinding

    private val viewModel: ShareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.shareToolbar)
        initView()
        initEvent()
    }

    private fun initView() {
        updateShareButtonState()
        binding.shareToolbar.setNavigationOnClickListener { finish() }
        binding.shareButton.setOnClickListener {
            AccountManager.checkLogin(this) {
                binding.loginLoading.isVisible = true
                viewModel.share(
                    viewModel.shareTitle.value ?: "",
                    viewModel.shareLink.value ?: ""
                )
            }
        }
    }

    private fun initEvent() {
        binding.shareArticleTitle.doOnTextChanged { text, _, _, _ ->
            viewModel.shareTitle.value = text.toString()
        }
        binding.shareLinkEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.shareLink.value = text.toString()
        }
        // 监听标题和链接变化
        viewModel.shareTitle.observe(this) { updateShareButtonState() }
        viewModel.shareLink.observe(this) { updateShareButtonState() }
        // 监听分享结果
        viewModel.shareLiveData.observe(this) {
            binding.loginLoading.isVisible = false  // 关闭登录进度条
            when (it.errorCode) {
                -1 -> { it.errorMsg.showShortToast() }
                0 -> { "分享成功".showShortToast() }
                else -> { "意外问题，请稍后再试！".showShortToast() }
            }
        }
    }

    private fun updateShareButtonState() {
        binding.shareButton.isEnabled = !viewModel.shareTitle.value.isNullOrBlank()
                && !viewModel.shareLink.value.isNullOrBlank()
    }
}