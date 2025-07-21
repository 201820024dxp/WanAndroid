package com.wanandroid.app.ui.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.model.Chapter
import com.wanandroid.app.logic.repository.GroupRepository

class GroupViewModel: ViewModel() {

    // 获取公众号标题
    val groupChapterList = liveData<List<Chapter>> {
        emit(GroupRepository.getGroupChapterList())
    }

}