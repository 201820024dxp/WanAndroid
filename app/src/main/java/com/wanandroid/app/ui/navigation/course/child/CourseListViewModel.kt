package com.wanandroid.app.ui.navigation.course.child

import androidx.lifecycle.ViewModel
import com.wanandroid.app.logic.repository.NavigationRepository

class CourseListViewModel : ViewModel() {

    fun getCourseListById(cid: Int) = NavigationRepository.getCourseListById(cid)

}