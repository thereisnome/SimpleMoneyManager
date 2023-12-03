package com.example.simplemoneymanager.presentation.recyclerViews.transactionList

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.TransactionItemBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.category.Category
import com.example.simplemoneymanager.domain.transaction.Transaction
import java.time.format.DateTimeFormatter

class TransactionListAdapter(private val itemClickListener: TransactionsPopupMenuItemClickListener) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    var onAccountClickListener: ((Account) -> Unit)? = null
    var onCategoryClickListener: ((Category) -> Unit)? = null

    var transactionList = listOf<Transaction>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        val account = transaction.account
        val category = transaction.category
        val balancePerDay = transactionList.filter { it.date == transaction.date }.sumOf { it.amount }
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val dateString = transaction.date.format(dateTimeFormatter)

        val shouldDisplayDate =
            position == 0 || transaction.date != transactionList[position - 1].date
        val shouldDisplayName = transaction.transactionName.isNotBlank()

        if (shouldDisplayDate) {
            holder.binding.tvDate.text = dateString
            holder.binding.tvBalancePerDay.text = Transaction.formatCurrency(balancePerDay)

            holder.binding.layoutDate.visibility = View.VISIBLE
        } else {
            holder.binding.layoutDate.visibility = View.GONE
        }

        val categoryContrast = ColorUtils.calculateContrast(
            holder.binding.tvCategory.currentHintTextColor,
            category.categoryColor.toColorInt()
        )

        if (categoryContrast < 1.5f) {
            holder.binding.tvCategory.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        val accountContrast = ColorUtils.calculateContrast(
            holder.binding.tvAccount.currentHintTextColor,
            account.accountColor.toColorInt()
        )

        if (accountContrast < 1.5f) {
            holder.binding.tvAccount.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.binding.tvAmount.text = Transaction.formatCurrency(transaction.amount)

        if (shouldDisplayName) {
            holder.binding.tvName.visibility = View.VISIBLE
            holder.binding.tvName.text = transaction.transactionName
        } else holder.binding.tvName.visibility = View.GONE
        holder.binding.tvCategory.text = category.categoryName
        holder.binding.tvCategory.backgroundTintList =
            ColorStateList.valueOf(category.categoryColor.toColorInt())
        holder.binding.tvCategory.setOnClickListener {
            onCategoryClickListener?.invoke(category)
        }

        holder.binding.tvAccount.text = account.accountName
        holder.binding.tvAccount.backgroundTintList =
            ColorStateList.valueOf(account.accountColor.toColorInt())
        holder.binding.tvAccount.setOnClickListener {
            onAccountClickListener?.invoke(account)
        }

        holder.itemView.setOnLongClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.itemView)
            popupMenu.inflate(R.menu.history_fragment_context_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val itemPosition = holder.adapterPosition
                itemClickListener.onMenuItemClick(
                    menuItem.itemId,
                    itemPosition,
                    transactionList[position]
                )
                true
            }
            popupMenu.show()
            true
        }
    }

    interface TransactionsPopupMenuItemClickListener {
        fun onMenuItemClick(itemId: Int, position: Int, transaction: Transaction)
    }
}