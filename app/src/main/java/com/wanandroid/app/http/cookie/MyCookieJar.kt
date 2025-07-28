package com.wanandroid.app.http.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class WanAndroidCookieJar(private val context: Context) : CookieJar {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WanAndroidCookies", Context.MODE_PRIVATE)

    /**
     * 发起网络请求前调用，在Request中设置Cookie
     */
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
//        val cookieString = sharedPreferences.getString(url.host, null)
//        val cookieList = ArrayList<Cookie>()
//        if (cookieString != null) {
//            val cookieArray = TextUtils.split(cookieString, ";")
//            for (cookieStr in cookieArray) {
//                val cookie = Cookie.parse(url, cookieStr)
//                cookie?.let { cookieList.add(it) }
//            }
//        }

        val cookieMaps = sharedPreferences.all
        Log.d(this.javaClass.simpleName, cookieMaps.toString())
        val validList = ArrayList<Cookie>()
        for (map in cookieMaps) {
            val cookie = Gson().fromJson(map.value as String, Cookie::class.java)
            if (cookie.expiresAt < System.currentTimeMillis()) {
                sharedPreferences.edit {
                    remove(map.key)
                    apply()
                }
            } else {
                validList.add(cookie)
            }
        }
        return validList
    }

    /**
     * 网络请求响应后调用，从Response中保存Cookie
     */
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        sharedPreferences.edit {
            for (cookie in cookies) {
                val key =
                    "${if (cookie.secure) "https" else "http"}://${cookie.domain}${cookie.path} | ${cookie.name}"
                if (sharedPreferences.contains(key)
                    && cookie.expiresAt < System.currentTimeMillis()
                ) {
                    remove(key)
                } else {
                    putString(key, Gson().toJson(cookie))
                }
            }
            apply()
        }


    }
}