package com.example.simplemoneymanager.presentation.recyclerViews.accountList

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.common.Format
import com.example.simplemoneymanager.data.database.models.TransactionDbModel
import com.example.simplemoneymanager.databinding.AccountListItemBinding
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.AccountWithTransactionsEntity
import java.time.LocalDate

class AccountListAdapter(private val itemClickListener: AccountPopupMenuItemClickListener) :
    RecyclerView.Adapter<AccountViewHolder>() {

    var onItemClickListener: ((AccountEntity) -> Unit)? = null

    private val format = Format()

    var accountWithTransactions = listOf<AccountWithTransactionsEntity>()
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
        val transactionList =
            accountWithTransactions[position].transactions.filter { LocalDate.parse(it.date).monthValue == LocalDate.now().monthValue }
        val accountIncomeValue =
            transactionList.filter { it.type == TransactionDbModel.INCOME }.sumOf { it.amount }
        val accountExpenseValue =
            transactionList.filter { it.type == TransactionDbModel.EXPENSE }.sumOf { it.amount }

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
        holder.binding.tvAccountDetailsBalance.text =
            format.formatCurrencyWithoutSign(account.balance)

        holder.binding.tvAccountDetailsIncomeValue.text =
            format.formatCurrency(accountIncomeValue)
        holder.binding.tvAccountDetailsExpenseValue.text =
            format.formatCurrency(accountExpenseValue)

        holder.binding.balanceLayout.backgroundTintList =
            ColorStateList.valueOf(account.accountColor.toColorInt())

        holder.binding.accountListItemLayout.setOnClickListener {
            onItemClickListener?.invoke(account)
        }

        holder.binding.accountListItemLayout.setOnLongClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.itemView)
            popupMenu.inflate(R.menu.account_bottom_sheet_dialog_fragment_context_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val itemPosition = holder.adapterPosition
                itemClickListener.onMenuItemClick(
                    menuItem.itemId,
                    itemPosition,
                    account
                )
                true
            }
            popupMenu.show()
            true
        }
    }

    interface AccountPopupMenuItemClickListener {
        fun onMenuItemClick(itemId: Int, position: Int, account: AccountEntity)
    }
}