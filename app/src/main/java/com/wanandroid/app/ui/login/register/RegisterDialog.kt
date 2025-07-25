package com.wanandroid.app.ui.login.register

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wanandroid.app.databinding.DialogRegisterBinding
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

    private fun initView() {
        updateLoginButtonState()
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
        // 注册按钮点击事件
        binding.registerButton.setOnClickListener {
            val username = viewModel.registerUserName.value ?: ""
            val password = viewModel.registerPassword.value ?: ""
            val confirm = viewModel.registerConfirm.value ?: ""
            if (checkRegisterStatus(username, password, confirm)) {
                viewModel.register(username, password, confirm)
            }
        }
        // 监听注册结果
        viewModel.registerLiveData.observe(this) {
            Log.d(this.javaClass.simpleName, it.toString())
            when (it.errorCode) {
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
            return false
        }
        if (password.length < 6) {
            binding.registerPwdLayout.error = "密码长度不能小于6"
            return false
        }
        if (confirm != password) {
            binding.registerConfirmLayout.error = "两次输入的密码不一致"
            return false
        }
        return true
    }
}