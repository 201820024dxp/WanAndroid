package com.wanandroid.app.ui.navigation.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.R
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemBinding
import com.wanandroid.app.ui.navigation.system.child.SystemChildContentFragment

class SystemChildFragment : BaseFragment<FragmentNavigatorChildSystemBinding>() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var chapterAdapter: SystemChapterAdapter
    private val viewModel : SystemChildViewModel by viewModels()

    // 保存当前展示的Fragment
    private var currentFragmentIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init views
        chapterAdapter = SystemChapterAdapter(emptyList())
        chapterAdapter.setOnItemClickListener { switchFragment(it) }
        linearLayoutManager = LinearLayoutManager(context)
        binding.systemTabRecyclerView.apply {
            adapter = chapterAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }

        // init events
        viewModel.systemDirectory.observe(viewLifecycleOwner) { chapterList ->
            // 更新适配器数据
            chapterAdapter.chapterList = chapterList
            chapterAdapter.notifyDataSetChanged()

            // 加载第一个Fragment
            switchFragment(0)

            // 加载完成隐藏进度条
            if (chapterList.isEmpty()) {
                binding.loadingContainer.emptyLayout.isVisible = true
            }
            binding.loadingContainer.loadingProgress.isVisible = false
        }
    }

    /**
     * 左侧一级目录的点击事件
     */
    fun switchFragment(index: Int) {
        if (!isAdded) return  // 检查Fragment是否已附加到Activity
        // 切换Fragment
        val fragment = SystemChildContentFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(SystemChildContentFragment.NAV_SYS_CONTENT_BUNDLE,
                viewModel.chapterList[index])
        }
        childFragmentManager.beginTransaction().apply {
            replace(R.id.systemFragmentView, fragment, fragment.javaClass.simpleName)
            currentFragmentIndex = index
            commit()
        }
        // 设置高亮
        chapterAdapter.setSelectedIndex(index)
    }

}