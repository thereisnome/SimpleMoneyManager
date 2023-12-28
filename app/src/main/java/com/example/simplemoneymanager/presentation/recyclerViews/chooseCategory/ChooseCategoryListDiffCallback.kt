package com.example.simplemoneymanager.presentation.recyclerViews.chooseCategory

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.category.CategoryEntity

class ChooseCategoryListDiffCallback: DiffUtil.ItemCallback<CategoryEntity>() {
    override fun areItemsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
        return oldItem == newItem
    }
}