package com.wanandroid.app.ui.navigation.child.navigation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemTextViewChipBinding
import com.wanandroid.app.logic.model.Navigation

class NavigationChapterAdapter(var navigationList: List<Navigation>) :
    RecyclerView.Adapter<NavigationChapterAdapter.ViewHolder>() {

    class ViewHolder(binding:ItemTextViewChipBinding) : RecyclerView.ViewHolder(binding.root) {
        val cLayout = binding.chipLayout
        val cTextView = binding.chipTextView
    }

    private var selectedIndex: Int = -1

    private var onItemClickListener:((Int) -> Unit) = { }

    fun setSelectedIndex(index: Int) {
        if (index in navigationList.indices) {
            selectedIndex = index
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = navigationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTextViewChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        // Set the layout parameters for the chip layout
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.chipLayout.layoutParams = layoutParams
        binding.chipLayout.strokeWidth = 0 // Set stroke width to 0 for no border
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val navigation = navigationList[position]
        // 隐藏链接列表为空的章节
        if (navigation.articles.isNotEmpty()) {
            // RecyclerView 会复用 ViewHolder，如果某次 onBindViewHolder 设置了高度为0（隐藏），
            // 下次复用时没有恢复高度，item 就会“缺失”
            holder.itemView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT // 恢复正常高度
            )
            holder.cTextView.text = navigation.name
//            holder.cLayout.isSelected = position == selectedIndex
            holder.cLayout.setCardBackgroundColor(
                if (position == selectedIndex) {
                    holder.cLayout.context.getColor(R.color.secondary_background_container)
                } else {
                    Color.TRANSPARENT
                }
            )
            holder.cLayout.setOnClickListener {
                // 点击Chapter item 高亮当前项并滚动右侧 RecyclerView
                onItemClickListener.invoke(position)
            }
        } else {
            // 如果没有文章，隐藏该条目
            holder.itemView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0 // 高度设置为0
            )
        }
    }

}