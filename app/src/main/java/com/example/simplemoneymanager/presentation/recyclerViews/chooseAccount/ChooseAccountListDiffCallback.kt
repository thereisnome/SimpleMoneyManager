package com.example.simplemoneymanager.presentation.recyclerViews.chooseAccount

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.account.Account

class ChooseAccountListDiffCallback: DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.accountId == newItem.accountId
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }
}