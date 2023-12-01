package com.example.simplemoneymanager.presentation.recyclerViews.chooseCategory

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.category.Category

class ChooseCategoryListDiffCallback: DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}