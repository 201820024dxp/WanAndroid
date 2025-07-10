package com.wanandroid.app.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivitySearchBinding
import com.wanandroid.app.ui.search.begin.SearchBeginFragment
import com.wanandroid.app.ui.search.begin.SearchBeginViewModel
import com.wanandroid.app.ui.search.result.SearchResultFragment

class SearchActivity : AppCompatActivity() {

    private val searchBeginFragment = SearchBeginFragment()
    private val searchResultFragment = SearchResultFragment()

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels()

    private val searchBeginViewModel: SearchBeginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }


        // 加载 fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.searchFragmentContainer,
                    searchBeginFragment,
                    searchBeginFragment.javaClass.simpleName)
                commit()
            }
        } else {
            supportFragmentManager.findFragmentByTag(SearchBeginFragment::class.java.simpleName)?.let {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.searchFragmentContainer, it, it.javaClass.simpleName)
                    commit()
                }
            }

            supportFragmentManager.findFragmentByTag(SearchResultFragment::class.java.simpleName)?.let {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.searchFragmentContainer, it, it.javaClass.simpleName)
                    commit()
                }
            }
        }

        // init view
        // 设置返回按钮
        binding.backImageButton.setOnClickListener {
            // 如果有返回栈，则弹出栈顶 fragment，否则结束 Activity
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }

        // 设置搜索图标
        binding.searchImageButton.setOnClickListener {
            val searchText = binding.searchEditText.text?.trim().toString()
            search(searchText)
        }

        // 设置搜索输入框
        binding.searchEditText.apply {
            // 监听输入法的"完成"动作
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    search(text?.trim().toString())
                    true
                } else {
                    false
                }
            }
            // 输入回车时发起搜索
            setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    search(text?.trim().toString())
                    true
                } else {
                    false
                }
            }
        }

        // init event
        // TODO: 监听 搜索热词 和 搜索历史 点击

    }

    // 搜索方法
    fun search(query: String) {
        if (query.isNotEmpty()) {
            binding.searchEditText.apply {
                clearFocus()    // 清除焦点
                // 隐藏软键盘
                val inputMethodManager =
                    this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
            }
            if (!searchResultFragment.isAdded) {
                supportFragmentManager.beginTransaction().apply {
                    hide(searchBeginFragment)   // 隐藏当前fragment
                    add(
                        R.id.searchFragmentContainer,
                        searchResultFragment,
                        searchResultFragment.javaClass.simpleName
                    )  // 添加搜索结果 fragment 到 Activity
                    addToBackStack(null)    // 添加到返回栈
                    commit()
                }
            }
            // 执行搜索
            viewModel.search(query)
            // 添加搜索记录
            searchBeginViewModel.addSearchHistory(query)
        }
    }

    // 点击 搜索热词 或 搜索历史 时，设置搜索输入框的文本
    fun setSearchEditText(text: String) {
        binding.searchEditText.setText(text)
        binding.searchEditText.setSelection(text.length) // 光标移到末尾
    }
}