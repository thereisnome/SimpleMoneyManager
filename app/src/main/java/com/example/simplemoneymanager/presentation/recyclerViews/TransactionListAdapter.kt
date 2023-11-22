package com.example.simplemoneymanager.presentation.recyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.databinding.TransactionItemBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionListAdapter() : RecyclerView.Adapter<TransactionViewHolder>() {
    private var shouldDisplayDate = false
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
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val dateString = transaction.date.format(dateTimeFormatter)

        shouldDisplayDate = position == 0 || transaction.date != transactionList[position-1].date

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
    }

    private fun formatIncome(value: Int): String {
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
        return "+$formattedAmount".replace(" руб.", "₽")
    }

    private fun formatExpense(value: Int): String {
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
        return "-$formattedAmount".replace(" руб.", "₽")
    }
}