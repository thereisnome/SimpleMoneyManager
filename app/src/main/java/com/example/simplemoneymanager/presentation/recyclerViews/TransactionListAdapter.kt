package com.example.simplemoneymanager.presentation.recyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.TransactionItemBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import java.time.format.DateTimeFormatter

class TransactionListAdapter(private val itemClickListener: TransactionsPopupMenuItemClickListener) :
    RecyclerView.Adapter<TransactionViewHolder>() {

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
        val balancePerDay = transactionList.filter { it.date == transaction.date }.sumOf { it.amount }
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val dateString = transaction.date.format(dateTimeFormatter)

        val shouldDisplayDate =
            position == 0 || transaction.date != transactionList[position - 1].date
        val shouldDisplayName = transaction.transactionName.isNotBlank()

        if (shouldDisplayDate) {
            holder.binding.tvDate.text = dateString
            if (balancePerDay >= 0){
                holder.binding.tvBalancePerDay.text = Transaction.formatIncome(balancePerDay)
            } else {
                holder.binding.tvBalancePerDay.text = Transaction.formatExpense(balancePerDay)
            }
            holder.binding.llDate.visibility = View.VISIBLE
        } else {
            holder.binding.llDate.visibility = View.GONE
        }

        when (transaction.type) {
            Transaction.INCOME -> holder.binding.tilAmount.text = Transaction.formatIncome(transaction.amount)
            Transaction.EXPENSE -> holder.binding.tilAmount.text = Transaction.formatExpense(transaction.amount)
        }

        if (shouldDisplayName) {
            holder.binding.tvName.visibility = View.VISIBLE
            holder.binding.tvName.text = transaction.transactionName
        } else holder.binding.tvName.visibility = View.GONE
        holder.binding.tvCategory.text = transaction.category.categoryName
        holder.binding.tvAccount.text = transaction.account.accountName

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