package com.wanandroid.app.ui.navigation.system.child

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemContentSubBinding
import com.wanandroid.app.logic.model.SystemSubDirectory
import com.wanandroid.app.ui.home.item.HomeArticleAdapter
import com.wanandroid.app.ui.home.item.HomeArticleDiffCallback
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SystemChildContentSubFragment : BaseFragment<FragmentNavigatorChildSystemContentSubBinding>() {

    companion object {
        const val NAV_SYS_CONTENT_SUB_BUNDLE = "nav_sys_content_sub_bundle"
    }

    val viewModel: SystemChildContentSubViewModel by viewModels()

    private val subDirectory by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.let { BundleCompat.getParcelable(it, NAV_SYS_CONTENT_SUB_BUNDLE, SystemSubDirectory::class.java) }
            ?: SystemSubDirectory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildSystemContentSubBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(NAV_SYS_CONTENT_SUB_BUNDLE) }?.apply {
//            val subDirectory = getParcelable<SystemSubDirectory>(NAV_SYS_CONTENT_SUB_BUNDLE)

            // init view
            val articleAdapter = HomeArticleAdapter(requireContext(), HomeArticleDiffCallback)
            binding.systemSubContentRecyclerView.apply {
                adapter = articleAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }

            // init event
            Log.d("SystemChildContentSubFragment", "subDirectory: $subDirectory")
            viewLifecycleOwner.lifecycleScope.apply {
                launch {
                    viewModel.getSystemArticleList(subDirectory?.id ?: 0)
                        .collectLatest { pagingData ->
                            Log.d("SystemChildContentSubFragment", "cid: ${subDirectory?.id}")
                            articleAdapter.submitData(pagingData)
                        }
                }
            }
//        }
    }

}