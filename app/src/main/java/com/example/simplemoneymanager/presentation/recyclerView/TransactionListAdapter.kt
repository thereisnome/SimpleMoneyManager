package com.example.simplemoneymanager.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.databinding.TransactionItemBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import java.time.format.DateTimeFormatter

class TransactionListAdapter() : ListAdapter<Transaction, TransactionViewHolder>(TransactionItemDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        val formatter = DateTimeFormatter.ofPattern("dd.MMMM.yyyy")
        val dateString = transaction.date.format(formatter)

        if (position == 0 || getItem(position-1).date != transaction.date){
            holder.binding.tvDate.text = dateString
            holder.binding.llDate.visibility = View.VISIBLE
        } else {
            holder.binding.llDate.visibility = View.GONE
        }

        holder.binding.tvAmount.text = transaction.amount.toString()
        holder.binding.tvCategory.text = transaction.category.name
        holder.binding.tvName.text = transaction.name
    }
}