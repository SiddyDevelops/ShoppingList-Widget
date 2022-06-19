package com.siddydevelops.shoppinglist.data.repositories

import com.siddydevelops.shoppinglist.data.db.ShoppingDatabase
import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem

class ShoppingRepository(
    private val db: ShoppingDatabase
) {
    suspend fun upSort(item: ShoppingItem) = db.getShoopingDao().upSort(item)

    suspend fun delete(item: ShoppingItem) = db.getShoopingDao().delete(item)

    suspend fun updateAmount(newAmount: Int, itemName: String) = db.getShoopingDao().updateAmount(newAmount,itemName)

    fun getAllShoppingItems() = db.getShoopingDao().getAllShoppingItems()
}