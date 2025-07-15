package com.wanandroid.app.ui.navigation.child.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildNavigatorBinding

class NavigationChildFragment : BaseFragment<FragmentNavigatorChildNavigatorBinding>() {

    private val viewModel: NavigationChildViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildNavigatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init views
        val childAdapter = NavigationChildAdapter(emptyList())
        val linearLayoutManager = LinearLayoutManager(context)
        binding.navChildRecyclerView.apply {        // 初始化 RecyclerView
            layoutManager = linearLayoutManager
            adapter = childAdapter
            setHasFixedSize(true)
        }

        // init events
        // Observe navigation list changes
        viewModel.navigationListLiveData.observe(viewLifecycleOwner) { navigationList ->
            // 更新适配器数据
            childAdapter.navigationList = navigationList
            childAdapter.notifyDataSetChanged()
            // 隐藏加载动画
            binding.loadingContainer.loadingProgress.visibility = View.GONE
        }

        // TODO: 下拉刷新
    }

}