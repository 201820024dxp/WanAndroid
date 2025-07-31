package com.wanandroid.app.logic.model

data class Collect(
    val author: String = "",
    val chapterId: Int = 0,
    val chapterName: String = "",
    val courseId: Int = 0,
    val desc: String = "",
    val envelopePic: String = "",
    val id: Int = 0,
    val link: String = "",
    val niceDate: String = "",
    val origin: String = "",
    val originId: Int = 0,
    val publishTime: Long = 0L,
    val title: String = "",
    val userId: Int = 0,
    val visible: Int = 0,
    val zan: Int = 0
) {
    var collect: Boolean = true
}
