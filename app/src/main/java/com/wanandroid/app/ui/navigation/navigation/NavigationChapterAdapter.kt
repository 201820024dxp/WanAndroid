package com.wanandroid.app.ui.navigation.navigation

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

    class ViewHolder(binding: ItemTextViewChipBinding) : RecyclerView.ViewHolder(binding.root) {
        val cLayout = binding.chipLayout
        val cTextView = binding.chipTextView
    }

    private var selectedIndex: Int = -1
    private companion object {
        private const val PAYLOAD_SELECTION_CHANGED = "selection_changed"
    }

    private var onItemClickListener: ((Int) -> Unit) = { }

    fun setSelectedIndex(index: Int) {
//        if (index in navigationList.indices) {
//            selectedIndex = index
//            notifyDataSetChanged()
//        }
        if (index in navigationList.indices && index != selectedIndex) {
            val oldIndex = selectedIndex
            selectedIndex = index

            // 只更新选中状态变化的项
            if (oldIndex >= 0) {
                notifyItemChanged(oldIndex, PAYLOAD_SELECTION_CHANGED)
            }
            notifyItemChanged(index, PAYLOAD_SELECTION_CHANGED)
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = navigationList.size

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
        val navigation = navigationList[position]
        holder.cTextView.text = navigation.name
        updateItemSelection(holder, position)
        holder.cLayout.setOnClickListener {
            // 点击Chapter item 高亮当前项并滚动右侧 RecyclerView
            onItemClickListener.invoke(position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            // 只更新背景色
            updateItemSelection(holder, position)
            return
        }

        // 完整更新
        onBindViewHolder(holder, position)
    }

    private fun updateItemSelection(holder: ViewHolder, position: Int) {
        holder.cLayout.setCardBackgroundColor(
            if (position == selectedIndex) {
                holder.cLayout.context.getColor(R.color.secondary_background_container)
            } else {
                Color.TRANSPARENT
            }
        )
    }
}