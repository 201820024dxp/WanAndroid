package com.wanandroid.app.ui.navigation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wanandroid.app.ui.navigation.course.CourseChildFragment
import com.wanandroid.app.ui.navigation.navigation.NavigationChildFragment
import com.wanandroid.app.ui.navigation.system.SystemChildFragment

class NavigationFragmentStateAdapter(private val tabList: List<String>, fragment: NavigationFragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabList.size

    override fun createFragment(position: Int): Fragment {
        return when(tabList[position]) {
            NavigationFragment.TAB_CHILD_NAVIGATOR -> NavigationChildFragment()
            NavigationFragment.TAB_CHILD_SYSTEM -> SystemChildFragment()
            NavigationFragment.TAB_CHILD_COURSE -> CourseChildFragment()
            else -> throw IllegalArgumentException("Invalid navigator tab: ${tabList[position]}")
        }
    }

}