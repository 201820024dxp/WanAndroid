package com.wanandroid.app.ui.project

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.model.Chapter
import com.wanandroid.app.logic.repository.ProjectRepository
import com.wanandroid.app.ui.project.child.ProjectChildFragment

class ProjectViewModel : ViewModel() {

    val projectTitleList = liveData<List<Chapter>> {
        emit(
            mutableListOf<Chapter>().apply {
                add(Chapter(id = ProjectChildFragment.PROJECT_ID_NEWEST, name = "最新项目"))
                addAll(ProjectRepository.getProjectTitleList())
            }
        )
    }

    // 监听父页面刷新事件
    val onProjectRefresh = MutableLiveData<Int>()

}