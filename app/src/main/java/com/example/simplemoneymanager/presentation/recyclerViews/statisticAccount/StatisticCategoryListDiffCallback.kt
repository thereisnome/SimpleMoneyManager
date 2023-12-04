package com.example.simplemoneymanager.presentation.recyclerViews.statisticAccount

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.category.Category

class StatisticCategoryListDiffCallback: DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}