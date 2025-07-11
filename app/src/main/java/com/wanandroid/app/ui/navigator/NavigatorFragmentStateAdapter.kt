package com.wanandroid.app.ui.navigator

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wanandroid.app.ui.navigator.child.course.CourseChildFragment
import com.wanandroid.app.ui.navigator.child.navigator.NavigatorChildFragment
import com.wanandroid.app.ui.navigator.child.system.SystemChildFragment

class NavigatorFragmentStateAdapter(private val tabList: List<String>, fragment: NavigatorFragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabList.size

    override fun createFragment(position: Int): Fragment {
        return when(tabList[position]) {
            NavigatorFragment.TAB_CHILD_NAVIGATOR -> NavigatorChildFragment()
            NavigatorFragment.TAB_CHILD_SYSTEM -> SystemChildFragment()
            NavigatorFragment.TAB_CHILD_COURSE -> CourseChildFragment()
            else -> throw IllegalArgumentException("Invalid navigator tab: ${tabList[position]}")
        }
    }

}