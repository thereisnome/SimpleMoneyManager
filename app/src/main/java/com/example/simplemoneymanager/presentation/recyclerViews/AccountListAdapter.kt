package com.example.simplemoneymanager.presentation.recyclerViews

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.AccountItemBinding
import com.example.simplemoneymanager.domain.account.Account

class AccountListAdapter(private val itemClickListener: AccountPopupMenuItemClickListener) :
    ListAdapter<Account, AccountViewHolder>(AccountListDiffCallback()) {

    var onItemClickListener: ((Account) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding =
            AccountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder((binding))
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = getItem(position)

        val contrast = ColorUtils.calculateContrast(
            holder.binding.tvAccountName.currentHintTextColor,
            account.accountColor.toColorInt()
        )

        if (contrast < 1.5f){
            holder.binding.tvAccountName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }

        holder.binding.tvAccountName.text = account.accountName
        holder.binding.tvAccountName.backgroundTintList =
            ColorStateList.valueOf(account.accountColor.toColorInt())

        holder.binding.tvAccountName.setOnClickListener {
            onItemClickListener?.invoke(account)
        }

        holder.binding.tvAccountName.setOnLongClickListener {
            val popupMenu = PopupMenu(holder.itemView.context, holder.itemView)
            popupMenu.inflate(R.menu.account_bottom_sheet_dialog_fragment_context_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val itemPosition = holder.adapterPosition
                itemClickListener.onMenuItemClick(
                    menuItem.itemId,
                    itemPosition,
                    currentList[position]
                )
                true
            }
            popupMenu.show()
            true
        }
    }

    interface AccountPopupMenuItemClickListener {
        fun onMenuItemClick(itemId: Int, position: Int, account: Account)
    }
}