package com.wanandroid.app.logic.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SystemTopDirectory (
    val author : String,
    val children: List<SystemSubDirectory>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Parcelable