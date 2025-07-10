package com.wanandroid.app.ui.search.begin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter(val searchBeginFragment: SearchBeginFragment,
                           val searchHistoryList: List<String>)
    : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = searchHistoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keyword = searchHistoryList[position]
        holder.binding.apply {
            searchHistoryTextView.text = keyword
            root.setOnClickListener { // 处理item点击事件，发起搜索
                searchBeginFragment.onSearchHistoryClick(keyword)
            }
            removeHistoryButton.setOnClickListener { // 处理删除按钮点击事件
                searchBeginFragment.onSearchHistoryDeleteClick(keyword)
            }
        }
    }
}