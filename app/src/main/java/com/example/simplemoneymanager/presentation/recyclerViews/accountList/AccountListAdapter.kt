package com.example.simplemoneymanager.presentation.recyclerViews.accountList

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.AccountListItemBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.account.AccountWithTransactions
import com.example.simplemoneymanager.domain.transaction.Transaction

class AccountListAdapter : RecyclerView.Adapter<AccountViewHolder>() {

    var onItemClickListener: ((Account) -> Unit)? = null

    var accountWithTransactions = listOf<AccountWithTransactions>()
        set(value) {
            val callback = AccountListDiffCallback(accountWithTransactions.map { it.account },
                value.map { it.account })
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding =
            AccountListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder((binding))
    }

    override fun getItemCount(): Int {
        return accountWithTransactions.map { it.account }.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accountWithTransactions.map { it.account }[position]
        val transactionList = accountWithTransactions[position].transactions
        val accountIncomeValue =
            transactionList.filter { it.type == Transaction.INCOME }.sumOf { it.amount }
        val accountExpenseValue =
            transactionList.filter { it.type == Transaction.EXPENSE }.sumOf { it.amount }

        val contrast = ColorUtils.calculateContrast(
            holder.binding.tvAccountDetailsName.currentHintTextColor,
            account.accountColor.toColorInt()
        )

        if (contrast < 1.5f) {
            holder.binding.tvAccountDetailsName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
            holder.binding.tvAccountDetailsBalance.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.binding.tvAccountDetailsName.text = account.accountName
        holder.binding.tvAccountDetailsBalance.text = Transaction.formatCurrencyWithoutSign(account.balance)

        holder.binding.tvAccountDetailsIncomeValue.text =
            Transaction.formatCurrency(accountIncomeValue)
        holder.binding.tvAccountDetailsExpenseValue.text =
            Transaction.formatCurrency(accountExpenseValue)

        holder.binding.balanceLayout.backgroundTintList =
            ColorStateList.valueOf(account.accountColor.toColorInt())

        holder.binding.accountDetailsLayout.setOnClickListener {
            onItemClickListener?.invoke(account)
        }
    }
}