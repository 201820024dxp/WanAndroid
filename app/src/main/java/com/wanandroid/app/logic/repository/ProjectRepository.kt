package com.wanandroid.app.logic.repository

import com.wanandroid.app.logic.network.impl.ProjectNetwork

object ProjectRepository {

    // 获取项目分类数据
    suspend fun getProjectTitleList() = ProjectNetwork.getProjectTitleList().data ?: emptyList()

}