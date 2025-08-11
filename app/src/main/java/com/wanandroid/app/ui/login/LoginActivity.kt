package com.wanandroid.app.ui.login

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.wanandroid.app.databinding.ActivityLoginBinding
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.ui.login.register.RegisterDialog
import com.wanandroid.app.utils.dp
import com.wanandroid.app.utils.showShortToast

class LoginActivity : AppCompatActivity() {

    private var lastImeVisible: Boolean = false
    private var lastImeHeight: Int = 0

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 观察软键盘可见性变化
        ViewCompat.setOnApplyWindowInsetsListener(binding.loginScrollView) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            lastImeHeight = imeHeight
            lastImeVisible = imeVisible
            Log.d(this.javaClass.simpleName, "imeVisible: $imeVisible, imeHeight: $imeHeight")
            // 偏移位置
            adjustOffsetForEditText(lastImeHeight, lastImeVisible)
            insets
        }

        initView()
        initEvent()
    }

    private fun initView() {
        updateLoginButtonState()
        // 返回按钮点击事件
        binding.loginBackButton.setOnClickListener { finish() }
        // 登录点击事件
        binding.loginButton.setOnClickListener {
            binding.loginLoading.isVisible = true   // 展示登录进度条
            viewModel.login(
                viewModel.loginUserName.value ?: "",
                viewModel.loginPassword.value ?: ""
            )
        }
        // 进入Activity，首个EditText直接获取焦点，并显示软键盘
        binding.loginUsernameEditText.requestFocus()
        WindowCompat.getInsetsController(window, binding.loginUsernameEditText)
            .show(WindowInsetsCompat.Type.ime())
        // 点击空白处隐藏软键盘
        arrayOf(binding.main, binding.loginLinearLayout).forEach { group ->
            group.setOnClickListener {
                val insets = ViewCompat.getRootWindowInsets(it) ?: return@setOnClickListener
                currentFocus?.clearFocus()
                if (insets.isVisible(WindowInsetsCompat.Type.ime())) {
                    WindowCompat.getInsetsController(window, it).hide(WindowInsetsCompat.Type.ime())
                }
            }
        }
        // 切换焦点重新计算偏移量(部分机型切换焦点时不触发软键盘可见性变化)
        arrayOf(binding.loginUsernameEditText, binding.loginPwdEditText).forEach {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    // 延迟一点等键盘稳定再计算偏移
                    it.postDelayed({
                        adjustOffsetForEditText(lastImeHeight, lastImeVisible)
                    }, 100)
                }
            }
        }
    }

    private fun initEvent() {
        // 监听用户名与密码的改变
        binding.loginUsernameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.loginUserName.value = text.toString()
        }
        binding.loginPwdEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.loginPassword.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        // 监听LiveData变化
        viewModel.loginUserName.observe(this) { updateLoginButtonState() }
        viewModel.loginPassword.observe(this) { updateLoginButtonState() }
        // 监听登录结果
        viewModel.loginLiveData.observe(this) {
            Log.d(this.javaClass.simpleName, it.toString())
            binding.loginLoading.isVisible = false  // 关闭登录进度条
            when (it?.errorCode) {
                -1 -> {
                    it.errorMsg.showShortToast()
                }

                0 -> {
                    // 保存登录cookie，修改登录状态，退出登录页面
                    AccountManager.setLoginStatus(true)
                    finish()
                }

                else -> {
                    "意外问题，请稍后再试！".showShortToast()
                }
            }
        }
        // 注册账号 点击事件
        binding.register.setOnClickListener {
            RegisterDialog().show(supportFragmentManager, "REGISTER_DIALOG")
        }
    }

    private fun updateLoginButtonState() {
        binding.loginButton.isEnabled = !viewModel.loginUserName.value.isNullOrBlank()
                && !viewModel.loginPassword.value.isNullOrBlank()
    }

    private fun adjustOffsetForEditText(imeHeight: Int, isImeVisible: Boolean) {
        if (isImeVisible) {
            // 键盘出现时，获取当前焦点的 EditText
            val focusedView = currentFocus ?: return

            val location = IntArray(2)  // location[0] represent x-loc, [1] represent y-loc
            focusedView.getLocationOnScreen(location)
            val editTextBottom = location[1] + focusedView.height

            // 如果键盘已经显示，则获取已经移动的距离
            val translationOffset = binding.loginScrollView.translationY

            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            val imeTop = screenHeight - imeHeight

            // 键盘顶部与EditText之间的距离
            val desiredOffset = editTextBottom + 100.dp - imeTop
            // 计算需要移动的距离（但需要考虑当前已经偏移过多少）
            val delta = desiredOffset - translationOffset   // 应该偏移的量 - 已经偏移的量
            Log.d(this.javaClass.simpleName, "delta: $delta")

            // 移动布局
            if (delta > 0) {
                binding.loginScrollView.animate()
                    .translationY(-delta).setDuration(200).start()
            }
        } else {
            // 键盘隐藏时重置滚动
            binding.loginScrollView.animate()
                .translationY(0F).setDuration(200).start()
        }
    }
}