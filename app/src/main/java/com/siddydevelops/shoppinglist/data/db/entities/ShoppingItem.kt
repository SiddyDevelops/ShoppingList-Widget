package com.siddydevelops.shoppinglist.data.db.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items", indices = [Index(value = ["item_name"], unique = true)])
data class ShoppingItem(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "item_name")
    val name: String,
    @ColumnInfo(name = "item_amount")
    var amount: Int
) {
}
