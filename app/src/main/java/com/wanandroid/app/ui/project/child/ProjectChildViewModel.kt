package com.wanandroid.app.ui.project.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.repository.ProjectRepository

class ProjectChildViewModel : ViewModel() {

    // 获取最新项目列表
    val getNewestProjectFlow =
        ProjectRepository.getNewestProjectList(20).cachedIn(viewModelScope)

    // 获取指定类别的项目列表
    fun getProjectListFlow(categoryId: Int) =
        ProjectRepository.getProjectListById(categoryId, 20).cachedIn(viewModelScope)
}