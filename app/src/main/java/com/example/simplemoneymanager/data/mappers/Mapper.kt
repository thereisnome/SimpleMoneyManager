package com.example.simplemoneymanager.data.mappers

import com.example.simplemoneymanager.data.database.models.AccountDbModel
import com.example.simplemoneymanager.data.database.models.AccountWithTransactionsDbModel
import com.example.simplemoneymanager.data.database.models.BudgetDbModel
import com.example.simplemoneymanager.data.database.models.BudgetWithTransactionsDbModel
import com.example.simplemoneymanager.data.database.models.CategoryDbModel
import com.example.simplemoneymanager.data.database.models.CategoryWithTransactionsDbModel
import com.example.simplemoneymanager.data.database.models.TransactionDbModel
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.domain.account.AccountWithTransactionsEntity
import com.example.simplemoneymanager.domain.budget.BudgetEntity
import com.example.simplemoneymanager.domain.budget.BudgetWithTransactionsEntity
import com.example.simplemoneymanager.domain.category.CategoryEntity
import com.example.simplemoneymanager.domain.category.CategoryWithTransactionsEntity
import com.example.simplemoneymanager.domain.transaction.TransactionEntity
import java.time.LocalDate
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapTransactionEntityToDbModel(transactionEntity: TransactionEntity) = TransactionDbModel(
        transactionId = transactionEntity.transactionId,
        type = transactionEntity.type,
        transactionName = transactionEntity.transactionName,
        category = mapCategoryEntityToDbModel(transactionEntity.category),
        amount = transactionEntity.amount,
        account = mapAccountEntityToDbModel(transactionEntity.account),
        date = LocalDate.parse(transactionEntity.date)
    )

    fun mapCategoryEntityToDbModel(categoryEntity: CategoryEntity) = CategoryDbModel(
        id = categoryEntity.id,
        categoryType = categoryEntity.categoryType,
        categoryName = categoryEntity.categoryName,
        categoryColor = categoryEntity.categoryColor
    )

    fun mapAccountEntityToDbModel(accountEntity: AccountEntity) = AccountDbModel(
        accountId = accountEntity.accountId,
        accountName = accountEntity.accountName,
        balance = accountEntity.balance,
        accountColor = accountEntity.accountColor
    )

    fun mapBudgetEntityToDbModel(budgetEntity: BudgetEntity) = BudgetDbModel(
        budgetId = budgetEntity.budgetId,
        maxValue = budgetEntity.maxValue,
        category = mapCategoryEntityToDbModel(budgetEntity.category)
    )

    fun mapTransactionDbModelToEntity(transactionDbModel: TransactionDbModel) = TransactionEntity(
        transactionId = transactionDbModel.transactionId,
        type = transactionDbModel.type,
        transactionName = transactionDbModel.transactionName,
        category = mapCategoryDbModelToEntity(transactionDbModel.category),
        amount = transactionDbModel.amount,
        account = mapAccountDbModelToEntity(transactionDbModel.account),
        date = transactionDbModel.date.toString()
    )

    fun mapCategoryDbModelToEntity(categoryDbModel: CategoryDbModel) = CategoryEntity(
        id = categoryDbModel.id,
        categoryType = categoryDbModel.categoryType,
        categoryName = categoryDbModel.categoryName,
        categoryColor = categoryDbModel.categoryColor
    )

    fun mapAccountDbModelToEntity(accountDbModel: AccountDbModel) = AccountEntity(
        accountId = accountDbModel.accountId,
        accountName = accountDbModel.accountName,
        balance = accountDbModel.balance,
        accountColor = accountDbModel.accountColor
    )

    fun mapBudgetDbModelToEntity(budgetDbModel: BudgetDbModel) = BudgetEntity(
        budgetId = budgetDbModel.budgetId,
        maxValue = budgetDbModel.maxValue,
        category = mapCategoryDbModelToEntity(budgetDbModel.category)
    )

    fun mapAccountWithTransactionsEntityToDbModel(accountWithTransactionsEntity: AccountWithTransactionsEntity) =
        AccountWithTransactionsDbModel(
            account = mapAccountEntityToDbModel(accountWithTransactionsEntity.account),
            transactions = accountWithTransactionsEntity.transactions.map {
                mapTransactionEntityToDbModel(
                    it
                )
            }
        )

    fun mapCategoryWithTransactionsEntityToDbModel(categoryWithTransactionsEntity: CategoryWithTransactionsEntity) =
        CategoryWithTransactionsDbModel(
            category = mapCategoryEntityToDbModel(categoryWithTransactionsEntity.category),
            transactions = categoryWithTransactionsEntity.transactions.map {
                mapTransactionEntityToDbModel(it)
            }
        )

    fun mapBudgetWithTransactionsEntityToDbModel(budgetWithTransactionsEntity: BudgetWithTransactionsEntity) =
        BudgetWithTransactionsDbModel(
            budget = mapBudgetEntityToDbModel(budgetWithTransactionsEntity.budget),
            transactions = budgetWithTransactionsEntity.transactions.map {
                mapTransactionEntityToDbModel(it)
            }
        )

    fun mapAccountWithTransactionsDbModelToEntity(accountWithTransactionsDbModel: AccountWithTransactionsDbModel) =
        AccountWithTransactionsEntity(
            account = mapAccountDbModelToEntity(accountWithTransactionsDbModel.account),
            transactions = accountWithTransactionsDbModel.transactions.map {
                mapTransactionDbModelToEntity(
                    it
                )
            }
        )

    fun mapCategoryWithTransactionsDbModelToEntity(categoryWithTransactionsDbModel: CategoryWithTransactionsDbModel) =
        CategoryWithTransactionsEntity(
            category = mapCategoryDbModelToEntity(categoryWithTransactionsDbModel.category),
            transactions = categoryWithTransactionsDbModel.transactions.map {
                mapTransactionDbModelToEntity(it)
            }
        )

    fun mapBudgetWithTransactionsDbModelToEntity(budgetWithTransactionsDbModel: BudgetWithTransactionsDbModel) =
        BudgetWithTransactionsEntity(
            budget = mapBudgetDbModelToEntity(budgetWithTransactionsDbModel.budget),
            transactions = budgetWithTransactionsDbModel.transactions.map {
                mapTransactionDbModelToEntity(it)
            }
        )
}