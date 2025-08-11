package com.wanandroid.app.ui.login.register

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wanandroid.app.databinding.DialogRegisterBinding
import com.wanandroid.app.utils.dp
import com.wanandroid.app.utils.showShortToast

class RegisterDialog : DialogFragment() {

    private lateinit var builder: MaterialAlertDialogBuilder
    private lateinit var binding: DialogRegisterBinding
    private lateinit var inflater: LayoutInflater

    private val viewModel: RegisterDialogViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            builder = MaterialAlertDialogBuilder(it)
            inflater = requireActivity().layoutInflater
            binding = DialogRegisterBinding.inflate(inflater)

            initView()
            initEvent()

            builder.setView(binding.root)
                .setCancelable(true)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        // 打开dialog，首个EditText直接获取焦点，并显示软键盘
        // dialog?.window 和 dialog?.currentFocus 只有在 onCreateDialog() 之后、并且对话框已展示时才会非空。
        // 在 onCreateView() 或 onViewCreated() 阶段它们可能仍为 null。
        // 通常建议在 onStart() 或之后的生命周期方法中访问这些对象：
        showSoftKeyboard(binding.registerUsernameEditText)
    }

    private fun initView() {
        updateLoginButtonState()
        // 注册按钮点击事件
        binding.registerButton.setOnClickListener {
            binding.registerLoading.isVisible = true    // 显示注册进度条
            val username = viewModel.registerUserName.value ?: ""
            val password = viewModel.registerPassword.value ?: ""
            val confirm = viewModel.registerConfirm.value ?: ""
            if (checkRegisterStatus(username, password, confirm)) {
                viewModel.register(username, password, confirm)
            }
        }
        // 点击空白处隐藏软键盘
        binding.registerLinearLayout.setOnClickListener {
            val insets = ViewCompat.getRootWindowInsets(it) ?: return@setOnClickListener
            requireDialog().currentFocus?.clearFocus()
            if (insets.isVisible(WindowInsetsCompat.Type.ime())) {
                WindowCompat.getInsetsController(requireActivity().window, it)
                    .hide(WindowInsetsCompat.Type.ime())
            }
        }
    }

    private fun initEvent() {
        // 监听输入框值的改变
        binding.registerUsernameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.registerUserName.value = text.toString()
            if (!binding.registerUserNameLayout.error.isNullOrBlank()) {
                binding.registerUserNameLayout.error = ""
            }
        }
        binding.registerPwdEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.registerPassword.value = text.toString()
            if (!binding.registerPwdLayout.error.isNullOrBlank()) {
                binding.registerPwdLayout.error = ""
            }
        }
        binding.registerConfirmEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.registerConfirm.value = text.toString()
            if (!binding.registerConfirmLayout.error.isNullOrBlank()) {
                binding.registerConfirmLayout.error = ""
            }
        }
        // 修改注册按钮状态
        viewModel.registerUserName.observe(this) { updateLoginButtonState() }
        viewModel.registerPassword.observe(this) { updateLoginButtonState() }
        viewModel.registerConfirm.observe(this) { updateLoginButtonState() }
        // 监听注册结果
        viewModel.registerLiveData.observe(this) {
            binding.registerLoading.isVisible = false
            Log.d(this.javaClass.simpleName, it.toString())
            when (it?.errorCode) {
                -1 -> { it.errorMsg.showShortToast() }
                0 -> {
                    "注册成功!".showShortToast()
                    dismiss()
                }
                else -> { "意外问题，请稍后再试！".showShortToast() }
            }
        }
    }

    private fun updateLoginButtonState() {
        binding.registerButton.isEnabled =
            !viewModel.registerUserName.value.isNullOrBlank()
                    && !viewModel.registerPassword.value.isNullOrBlank()
                    && !viewModel.registerConfirm.value.isNullOrBlank()
    }

    private fun checkRegisterStatus(
        username: String,
        password: String,
        confirm: String
    ) : Boolean {
        if (username.length < 3) {
            binding.registerUserNameLayout.error = "用户名长度不能小于3"
            binding.registerUsernameEditText.requestFocus()
            binding.registerLoading.isVisible = false    // 隐藏注册进度条
            return false
        }
        if (password.length < 6) {
            binding.registerPwdLayout.error = "密码长度不能小于6"
            binding.registerPwdEditText.requestFocus()
            binding.registerLoading.isVisible = false    // 隐藏注册进度条
            return false
        }
        if (confirm != password) {
            binding.registerConfirmLayout.error = "两次输入的密码不一致"
            binding.registerConfirmEditText.requestFocus()
            binding.registerLoading.isVisible = false    // 隐藏注册进度条
            return false
        }
        return true
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            requireDialog().window?.let {
                WindowCompat.getInsetsController(it, view).show(WindowInsetsCompat.Type.ime())
            }
        }
    }
}