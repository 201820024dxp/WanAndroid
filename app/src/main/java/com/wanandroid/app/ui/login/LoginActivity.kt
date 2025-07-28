package com.wanandroid.app.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.wanandroid.app.databinding.ActivityLoginBinding
import com.wanandroid.app.ui.login.register.RegisterDialog
import com.wanandroid.app.utils.showShortToast

class LoginActivity : AppCompatActivity() {

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

        // 软键盘弹出时位移
//        ViewCompat.setOnApplyWindowInsetsListener(binding.loginScrollView) { _, insets ->
//            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
//            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//            insets
//        }

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
            when (it.errorCode) {
                -1 -> {
                    it.errorMsg.showShortToast()
                }

                0 -> {
                    // TODO: 保存登录cookie，修改登录状态，退出登录页面

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
}