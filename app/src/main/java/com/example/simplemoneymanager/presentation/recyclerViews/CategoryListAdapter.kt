package com.example.simplemoneymanager.presentation.recyclerViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.simplemoneymanager.databinding.CategoryItemBinding
import com.example.simplemoneymanager.domain.category.Category

class CategoryListAdapter(): ListAdapter<Category, CategoryViewHolder>(CategoryListDiffCallback()) {

    var onItemClickListener: ((Category) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder((binding))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.binding.tvCategoryName.text = category.name

        holder.binding.tvCategoryName.setOnClickListener {
            onItemClickListener?.invoke(category)
        }
    }
}