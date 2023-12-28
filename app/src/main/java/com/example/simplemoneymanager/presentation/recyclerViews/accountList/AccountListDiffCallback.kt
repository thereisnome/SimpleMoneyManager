package com.example.simplemoneymanager.presentation.recyclerViews.accountList

import androidx.recyclerview.widget.DiffUtil
import com.example.simplemoneymanager.domain.account.AccountEntity

class AccountListDiffCallback(
    private val oldList: List<AccountEntity>,
    private val newList: List<AccountEntity>,
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
        return oldItem.accountId == newItem.accountId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}