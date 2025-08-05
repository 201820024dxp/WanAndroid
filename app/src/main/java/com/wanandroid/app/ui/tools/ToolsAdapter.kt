package com.wanandroid.app.ui.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemToolListLayoutBinding
import com.wanandroid.app.logic.model.Tool
import com.wanandroid.app.ui.web.WebActivity

class ToolsAdapter(var toolList: List<Tool>) : RecyclerView.Adapter<ToolsAdapter.ViewHolder>(){

    class ViewHolder(binding: ItemToolListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.toolIcon
        val name = binding.toolName
        val desc = binding.toolDesc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            ItemToolListLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        val tempContext = holder.itemView.context
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val tool = toolList[position]
            WebActivity.loadUrl(tempContext, tool.link)
        }
        return holder
    }

    override fun getItemCount(): Int = toolList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tool = toolList[position]
        Glide.with(holder.itemView) // Use Glide to load images
            .load(Tool.imagePrefix + tool.icon)
            .placeholder(R.drawable.ic_tool_48dp) // Placeholder image
            .into(holder.icon)
        holder.name.text = tool.name
        holder.desc.text = tool.desc
    }
}