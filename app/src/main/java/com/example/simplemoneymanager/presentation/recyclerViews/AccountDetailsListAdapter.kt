package com.example.simplemoneymanager.presentation.recyclerViews

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.AccountDetailsItemBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.transaction.Transaction

class AccountDetailsListAdapter(private val transactionList: List<Transaction>) :
    ListAdapter<Account, AccountDetailsViewHolder>(AccountDetailsListDiffCallback()) {

    var onItemClickListener: ((Account) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountDetailsViewHolder {
        val binding =
            AccountDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountDetailsViewHolder((binding))
    }

    override fun onBindViewHolder(holder: AccountDetailsViewHolder, position: Int) {
        val account = getItem(position)
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
            holder.binding.tvAccountDetailsValue.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.binding.tvAccountDetailsName.text = account.accountName
        holder.binding.tvAccountDetailsValue.text = Transaction.formatCurrency(account.balance)

        holder.binding.tvAccountDetailsIncomeValue.text = accountIncomeValue.toString()
        holder.binding.tvAccountDetailsExpenseValue.text = accountExpenseValue.toString()

        holder.binding.balanceLayout.backgroundTintList =
            ColorStateList.valueOf(account.accountColor.toColorInt())

        holder.binding.accountDetailsLayout.setOnClickListener {
            onItemClickListener?.invoke(account)
        }
    }
}