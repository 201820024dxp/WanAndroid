package com.wanandroid.app.ui.navigation.system.child

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wanandroid.app.logic.model.SystemSubDirectory

class SystemChildContentStateAdapter(
    val subDirectoryList: List<SystemSubDirectory>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = subDirectoryList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = SystemChildContentSubFragment().apply {
            arguments = Bundle().apply {
                putParcelable(
                    SystemChildContentSubFragment.NAV_SYS_CONTENT_SUB_BUNDLE,
                    subDirectoryList[position]
                )
            }
        }
        return fragment
    }
}