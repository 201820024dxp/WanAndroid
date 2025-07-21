package com.wanandroid.app.ui.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wanandroid.app.logic.model.Chapter
import com.wanandroid.app.ui.group.child.GroupChildFragment

class GroupFragmentStateAdapter(var groupChapterList: List<Chapter>, fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = groupChapterList.size

    override fun createFragment(position: Int): Fragment {
        val groupChildFragment = GroupChildFragment()
        groupChildFragment.arguments = Bundle().apply {
            putParcelable(GroupChildFragment.GROUP_CHILD_CHAPTER_BUNDLE, groupChapterList[position])
        }
        return groupChildFragment
    }
}