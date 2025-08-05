package com.wanandroid.app.app

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object AppPreferences {
    const val PREFS_NAME = "AppPrefs"
    const val THEME_KEY = "theme_mode"

    lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        // 获取保存的主题模式
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // 默认跟随系统
        val themeMode =
            sharedPreferences.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置主题
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    fun saveThemeMode(themeMode: Int) {
        // 保存用户选择的主题模式
        sharedPreferences.edit {
            putInt(THEME_KEY, themeMode)
        }
    }
}