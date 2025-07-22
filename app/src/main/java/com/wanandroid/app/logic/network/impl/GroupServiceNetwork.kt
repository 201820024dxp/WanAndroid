package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.GroupService
import retrofit2.await

object GroupServiceNetwork {
    private val groupService: GroupService by lazy { ServiceCreator.create<GroupService>() }

    suspend fun getGroupChapterList() = groupService.getGroupChapterList().await()

    suspend fun getGroupArticleListById(id: Int, page: Int, pageSize:Int = 20) =
        groupService.getGroupArticleListById(id, page, pageSize).await()
}