package com.wanandroid.app.ui.navigation.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.model.SystemTopDirectory
import com.wanandroid.app.logic.repository.NavigationRepository

class SystemChildViewModel : ViewModel() {

    var chapterList : List<SystemTopDirectory> = emptyList()

    val systemDirectory = liveData<List<SystemTopDirectory>> {
        val systemChapterList = NavigationRepository.getSystemChapterList()
        chapterList = systemChapterList
        emit(
            systemChapterList
        )
    }

}