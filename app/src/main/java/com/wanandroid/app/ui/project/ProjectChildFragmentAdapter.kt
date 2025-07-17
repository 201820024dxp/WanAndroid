package com.wanandroid.app.ui.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wanandroid.app.logic.model.ProjectTitle
import com.wanandroid.app.ui.project.child.ProjectChildFragment

class ProjectChildFragmentAdapter(var titleList: List<ProjectTitle>, fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = titleList.size

    override fun createFragment(position: Int): Fragment {
        val childFragment = ProjectChildFragment()
        childFragment.arguments = Bundle().apply {
            putParcelable(ProjectChildFragment.ARG_TITLE, titleList[position])
        }
        Log.d("ProjectChildFragmentAdapter", "Creating fragment for ${titleList[position].name}")
        return childFragment
    }
}