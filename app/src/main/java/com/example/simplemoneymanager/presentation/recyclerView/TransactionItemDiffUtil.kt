package com.example.simplemoneymanager.presentation.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.transaction.Transaction

class TransactionItemDiffUtil: DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}