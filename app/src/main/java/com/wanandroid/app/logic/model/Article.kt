package com.wanandroid.app.logic.model

import com.google.gson.annotations.Expose
import com.wanandroid.app.widget.BusinessMode

data class Article(
    var apkLink: String = "",
    var audit: Int = 0,
    var author: String = "",
    var canEdit: Boolean = false,
    var chapterId: Int  = 0,
    var chapterName: String = "",
    var collect: Boolean = false,
    var courseId: Int = 0,
    var desc: String = "",
    var descMd: String = "",
    var envelopePic: String = "",
    var fresh: Boolean = false,
    var host: String = "",
    var id: Int = 0,
    var isAdminAdd: Boolean = false,
    var link: String = "",
    var niceDate: String = "",
    var niceShareDate: String = "",
    var origin: String = "",
    var prefix: String = "",
    var projectLink: String = "",
    var publishTime: Long = 0L,
    var realSuperChapterId: Int = 0,
    var selfVisible: Int = 0,
    var shareDate: Long = 0L,
    var shareUser: String = "",
    var superChapterId: Int = 0,
    var superChapterName: String = "",
    var tags: List<Tag> = emptyList(),
    var title: String = "",
    var type: Int = 0,
    var userId: Int = 0,
    var visible: Int = 0,
    var zan: Int = 0,
    var buzMode: BusinessMode = BusinessMode.NORM
) {
    data class Tag(
        var name: String,
        var url: String
    )
}
