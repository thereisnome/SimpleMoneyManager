package com.example.simplemoneymanager.presentation.recyclerViews.chooseAccount

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.account.AccountEntity

class ChooseAccountListDiffCallback: DiffUtil.ItemCallback<AccountEntity>() {
    override fun areItemsTheSame(oldItem: AccountEntity, newItem: AccountEntity): Boolean {
        return oldItem.accountId == newItem.accountId
    }

    override fun areContentsTheSame(oldItem: AccountEntity, newItem: AccountEntity): Boolean {
        return oldItem == newItem
    }
}