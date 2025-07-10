package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.ProjectService
import retrofit2.await

object ProjectNetwork {

    private val projectService by lazy { ServiceCreator.create<ProjectService>() }

    suspend fun getProjectTitleList() = projectService.getProjectTitleList().await()

}