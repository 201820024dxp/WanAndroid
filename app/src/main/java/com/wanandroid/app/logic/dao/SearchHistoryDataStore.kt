package com.wanandroid.app.logic.dao

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "wan_android_data_store")

object SearchHistoryDataStore {

    const val KEY_DATA_STORE_SEARCH_HISTORY = "key_data_store_search_history"

    val searchHistoryKey =
        stringSetPreferencesKey(KEY_DATA_STORE_SEARCH_HISTORY)

}