package com.wanandroid.app.ui.search.begin

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.wanandroid.app.logic.model.HotKey
import com.wanandroid.app.ui.search.SearchActivity

class SearchHotKeyAdapter(val activity: SearchActivity, val hotKeyList: List<HotKey>) :
    RecyclerView.Adapter<SearchHotKeyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            Chip(parent.context).apply {
                layoutParams = FlexboxLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                isClickable = true
                isCheckable = false
            })
    }

    override fun getItemCount(): Int = hotKeyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotKey = hotKeyList[position]
        (holder.itemView as Chip).apply {
            text = hotKey.name
            setOnClickListener {
                activity.search(hotKey.name)
            }
        }
    }


}