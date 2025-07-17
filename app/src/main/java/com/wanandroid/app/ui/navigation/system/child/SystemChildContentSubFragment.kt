package com.wanandroid.app.ui.navigation.system.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildSystemContentSubBinding
import com.wanandroid.app.logic.model.SystemSubDirectory

class SystemChildContentSubFragment : BaseFragment<FragmentNavigatorChildSystemContentSubBinding>() {

    companion object {
        const val NAV_SYS_CONTENT_SUB_BUNDLE = "nav_sys_content_sub_bundle"
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
        arguments?.takeIf { it.containsKey(NAV_SYS_CONTENT_SUB_BUNDLE) }?.apply {
            val subDirectory =
                getParcelable<SystemSubDirectory>(NAV_SYS_CONTENT_SUB_BUNDLE)
            // binding.navSysContentSub.text = subDirectory?.name
        }
    }

}