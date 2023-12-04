package com.example.simplemoneymanager.presentation.recyclerViews.statisticAccount

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.CategoryItemBinding
import com.example.simplemoneymanager.domain.category.Category

class StatisticCategoryListAdapter() :
    ListAdapter<Category, StatisticCategoryViewHolder>(StatisticCategoryListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticCategoryViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatisticCategoryViewHolder((binding))
    }

    override fun onBindViewHolder(holder: StatisticCategoryViewHolder, position: Int) {
        val category = getItem(position)
        val contrast = ColorUtils.calculateContrast(
            holder.binding.tvCategoryName.currentHintTextColor,
            category.categoryColor.toColorInt()
        )

        if (contrast < 1.5f) {
            holder.binding.tvCategoryName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.binding.tvCategoryName.text = category.categoryName
        holder.binding.tvCategoryName.backgroundTintList =
            ColorStateList.valueOf(category.categoryColor.toColorInt())
    }
}