package com.wanandroid.app.ui.navigation.system

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemTextViewChipBinding
import com.wanandroid.app.logic.model.SystemTopDirectory

class SystemChapterAdapter(var chapterList: List<SystemTopDirectory>) :
    RecyclerView.Adapter<SystemChapterAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemTextViewChipBinding) : RecyclerView.ViewHolder(binding.root) {
        val cLayout = binding.chipLayout
        val cTextView = binding.chipTextView
    }

    private var selectedIndex: Int = -1

    private var onItemClickListener: ((Int) -> Unit) = { }

    fun setSelectedIndex(index: Int) {
        if (index in chapterList.indices) {
            selectedIndex = index
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = chapterList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTextViewChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
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
        val chapter = chapterList[position]
        holder.cTextView.apply{
            text = chapter.name
            // 实现跑马灯效果
            isSelected = position == selectedIndex
            ellipsize = TextUtils.TruncateAt.MARQUEE
            setSingleLine()
            marqueeRepeatLimit = 1  // 跑马灯仅循环一次，防止过分吸引注意力
        }
        holder.cLayout.setCardBackgroundColor(
            if (position == selectedIndex) {
                holder.cLayout.context.getColor(R.color.secondary_background_container)
            } else {
                Color.TRANSPARENT
            }
        )
        holder.cLayout.setOnClickListener {
            // 点击Chapter item 高亮当前项并新建右侧 Fragment
            onItemClickListener.invoke(position)
        }

    }

}