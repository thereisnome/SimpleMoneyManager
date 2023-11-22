package com.example.simplemoneymanager.presentation.recyclerViews

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.transaction.Transaction

class TransactionListDiffCallback: DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.transactionId == newItem.transactionId
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}