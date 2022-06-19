package com.siddydevelops.shoppinglist.ui.shopingList

import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClick(item: ShoppingItem)
}