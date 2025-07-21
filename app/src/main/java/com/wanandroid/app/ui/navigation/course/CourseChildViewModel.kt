package com.wanandroid.app.ui.navigation.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.repository.NavigationRepository

class CourseChildViewModel : ViewModel() {

    val courseChapterList = liveData {
        emit(NavigationRepository.getCourseChapterList())
    }

}