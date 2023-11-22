package com.example.simplemoneymanager.presentation.recyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.TransactionItemBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionListAdapter(private val itemClickListener: PopupMenuItemClickListener) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    var transactionList = listOf<Transaction>()
        set(value) {
            val callback = TransactionListDiffCallback(transactionList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
//            notifyDataSetChanged()
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
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val dateString = transaction.date.format(dateTimeFormatter)

        val shouldDisplayDate = position == 0 || transaction.date != transactionList[position-1].date

        if (shouldDisplayDate) {
            holder.binding.tvDate.text = dateString
            holder.binding.llDate.visibility = View.VISIBLE
        } else {
            holder.binding.llDate.visibility = View.GONE
        }

        when (transaction.type) {
            Transaction.INCOME -> holder.binding.tilAmount.text = formatIncome(transaction.amount)
            Transaction.EXPENSE -> holder.binding.tilAmount.text = formatExpense(transaction.amount)
        }

        holder.binding.tvCategory.text = transaction.category.name
        holder.binding.tvName.text = transaction.transactionName

        holder.itemView.setOnClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.itemView)
            popupMenu.inflate(R.menu.context_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val itemPosition = holder.adapterPosition
                itemClickListener.onMenuItemClick(menuItem.itemId, itemPosition, transactionList[position])
                true
            }
            popupMenu.show()
        }
    }

    private fun formatIncome(value: Int): String {
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
        return "+$formattedAmount".replace(" руб.", "₽")
    }

    private fun formatExpense(value: Int): String {
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
        return "-$formattedAmount".replace(" руб.", "₽")
    }

    interface PopupMenuItemClickListener {
        fun onMenuItemClick(itemId: Int, position: Int, transaction: Transaction)
    }
}