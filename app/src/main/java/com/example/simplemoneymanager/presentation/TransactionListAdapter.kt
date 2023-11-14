package com.example.simplemoneymanager.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.databinding.TransactionItemBinding
import com.example.simplemoneymanager.domain.transaction.Transaction

class TransactionListAdapter(private val transactionList: List<Transaction>) : RecyclerView.Adapter<TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.binding.tvAmount.text = transaction.amount.toString()
//        holder.binding.tvCategory.text = transaction.category.name
        holder.binding.tvName.text = transaction.name
    }


    override fun getItemCount(): Int {
        return transactionList.size
    }
}