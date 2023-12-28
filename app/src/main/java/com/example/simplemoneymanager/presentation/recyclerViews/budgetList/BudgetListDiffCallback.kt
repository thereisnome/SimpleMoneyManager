package com.example.simplemoneymanager.presentation.recyclerViews.budgetList

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.budget.BudgetEntity

class BudgetListDiffCallback(
    private val oldList: List<BudgetEntity>,
    private val newList: List<BudgetEntity>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.budgetId == newItem.budgetId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}