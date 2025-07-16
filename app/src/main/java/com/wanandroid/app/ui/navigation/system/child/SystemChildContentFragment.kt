package com.wanandroid.app.ui.navigation.system.child

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemContentBinding
import com.wanandroid.app.logic.model.ProjectTitle

class SystemChildContentFragment : BaseFragment<FragmentNavigatorChildSystemContentBinding>() {

    companion object {
        const val NAV_SYS_CONTENT_BUNDLE = "nav_sys_content_bundle"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildSystemContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO: 体系一级目录下的内容
        arguments?.takeIf { it.containsKey(NAV_SYS_CONTENT_BUNDLE) }?.apply {
            val project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getString(NAV_SYS_CONTENT_BUNDLE)
            } else {
                getString(NAV_SYS_CONTENT_BUNDLE)
            }
            Log.d("SystemChildContentFragment", project ?: "null")

            binding.systemContentText.text = project
        }
    }

}