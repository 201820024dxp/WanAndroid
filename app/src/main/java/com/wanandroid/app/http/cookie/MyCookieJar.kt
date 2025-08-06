package com.wanandroid.app.http.cookie

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Base64
import android.util.Base64.encodeToString
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class WanAndroidCookieJar(private val context: Context) : CookieJar {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WanAndroidCookies", Context.MODE_PRIVATE)

    private val secretKey = "wan_android" // 16 chars for AES-128
    private val encryptAlgorithm = "AES"

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
//            val decryptedCookie = decrypt(map.value as String)
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
                val key = "${cookie.domain} | ${cookie.name}"
                // 检查Cookie是否过期
                if (cookie.expiresAt < System.currentTimeMillis()) {
                    remove(key)
                } else {
//                    val encryptedCookie = encrypt(Gson().toJson(cookie))
                    putString(key, Gson().toJson(cookie))
                }
            }
            apply()
        }
    }

    @SuppressLint("GetInstance")
    private fun encrypt(input: String): String {
        val cipher = Cipher.getInstance(encryptAlgorithm)
        val keySpec = SecretKeySpec(secretKey.toByteArray(), encryptAlgorithm)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encrypted = cipher.doFinal(input.toByteArray())
        return encodeToString(encrypted, Base64.DEFAULT)
    }

    @SuppressLint("GetInstance")
    private fun decrypt(input: String): String {
        val cipher = Cipher.getInstance(encryptAlgorithm)
        val keySpec = SecretKeySpec(secretKey.toByteArray(), encryptAlgorithm)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decoded = Base64.decode(input, Base64.DEFAULT)
        val decrypted = cipher.doFinal(decoded)
        return String(decrypted)
    }
}