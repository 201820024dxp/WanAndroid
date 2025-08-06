package com.wanandroid.app.ui.tools

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.databinding.ActivityToolsBinding

class ToolsActivity : AppCompatActivity() {

    private lateinit var toolsAdapter: ToolsAdapter
    private lateinit var binding: ActivityToolsBinding
    private val viewModel: ToolsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityToolsBinding.inflate(layoutInflater)
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
        // 返回
        binding.toolbar.setNavigationOnClickListener { finish() }
        // 工具列表
        toolsAdapter = ToolsAdapter(emptyList())
        binding.toolsRecyclerView.apply {
            adapter = toolsAdapter
            layoutManager = LinearLayoutManager(this@ToolsActivity)
            setHasFixedSize(true)
        }
    }

    private fun initEvent() {
        viewModel.toolListLiveData.observe(this) { toolList ->
            // 更新工具列表
            toolsAdapter.toolList = toolList
            toolsAdapter.notifyDataSetChanged()
        }
    }
}