package com.wanandroid.app.logic.model

data class Tool(
    val desc: String = "",
    val icon: String = "",
    val id: Int = 0,
    val isNew: Int = 0,
    val link: String = "",
    val name: String = "",
    val order: Int = 0,
    val showInTab: Int = 0,
    val tabName: String = "",
    val visible: Int = 0
) {
    companion object {
        const val imagePrefix: String = "https://www.wanandroid.com/resources/image/pc/tools/"
    }
}
