package com.siddydevelops.shoppinglist.ui.shopingList

import androidx.lifecycle.ViewModel
import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem
import com.siddydevelops.shoppinglist.data.repositories.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {
    fun upSort(item: ShoppingItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.upSort(item)
    }

    fun delete(item: ShoppingItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
    }

    fun updateAmount(newAmount: Int, itemName: String) = CoroutineScope(Dispatchers.Main).launch {
        repository.updateAmount(newAmount,itemName)
    }

    fun getAllShoppingItems() = run { repository.getAllShoppingItems() }
}