package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.ProjectService
import retrofit2.await

object ProjectNetwork {

    private val projectService by lazy { ServiceCreator.create<ProjectService>() }

    suspend fun getProjectTitleList() = projectService.getProjectTitleList().await()

    suspend fun getNewestProjectList(pageNo: Int, pageSize: Int) =
        projectService.getNewestProjectList(pageNo, pageSize).await()

    suspend fun getProjectListById(pageNo: Int, categoryId: Int, pageSize: Int) =
        projectService.getProjectListById(pageNo, categoryId, pageSize).await()

}