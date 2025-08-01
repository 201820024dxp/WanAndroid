package com.wanandroid.app.ui.tools

import androidx.lifecycle.ViewModel
import com.wanandroid.app.logic.repository.ProfileRepository

class ToolsViewModel : ViewModel() {

    val toolListLiveData = ProfileRepository.getToolList()
}