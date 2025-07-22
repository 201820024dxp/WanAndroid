package com.wanandroid.app.ui.group.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.repository.GroupRepository

class GroupChildViewModel : ViewModel() {

    // 获取公众号文章列表
    fun getGroupArticleList(id: Int, pageSize: Int = 20) =
        GroupRepository.getGroupArticleList(id, pageSize).cachedIn(viewModelScope)

}