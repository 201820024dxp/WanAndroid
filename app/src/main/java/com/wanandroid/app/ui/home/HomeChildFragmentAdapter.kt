package com.wanandroid.app.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wanandroid.app.ui.home.child.answer.AnswerFragment
import com.wanandroid.app.ui.home.child.explore.ExploreFragment
import com.wanandroid.app.ui.home.child.square.SquareFragment

class HomeChildFragmentAdapter(
    var fragmentList: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        const val HOME_TAB_EXPLORE = "首页"
        const val HOME_TAB_SQUARE = "广场"
        const val HOME_TAB_ANSWER = "问答"
    }

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return when(fragmentList[position]) {
            is ExploreFragment -> fragmentList[position] as ExploreFragment
            is SquareFragment -> fragmentList[position] as SquareFragment
            is AnswerFragment -> fragmentList[position] as AnswerFragment
            else -> fragmentList[position]
        }
    }
}