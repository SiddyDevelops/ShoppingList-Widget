package com.siddydevelops.shoppinglist.ui.shopingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.siddydevelops.shoppinglist.data.repositories.ShoppingRepository

@Suppress("UNCHECKED_CAST")
class ShopingViewModelFactory(
    private val repository: ShoppingRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoppingViewModel(repository) as T
    }

}