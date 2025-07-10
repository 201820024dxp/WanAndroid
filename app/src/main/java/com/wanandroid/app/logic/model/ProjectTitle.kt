package com.wanandroid.app.logic.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectTitle(
    val author: String = "",
    val courseId: Int = 0,
    val cover: String = "",
    val desc: String = "",
    val id: Int = 0,
    val lisense: String = "",
    val lisenseLink: String = "",
    val name: String = "",
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val type: Int = 0,
    val userControlSetTop: Boolean = false,
    val visible: Int = 0
) : Parcelable
