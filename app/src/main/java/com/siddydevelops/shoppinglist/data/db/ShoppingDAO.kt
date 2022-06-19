package com.siddydevelops.shoppinglist.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem

@Dao
interface ShoppingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSort(item: ShoppingItem)

    @Query("UPDATE shopping_items SET item_amount=:newAmount WHERE item_name = :itemName")
    suspend fun updateAmount(newAmount: Int, itemName: String)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("SELECT * FROM shopping_items")
    fun getAllShoppingItems(): LiveData<List<ShoppingItem>>

}