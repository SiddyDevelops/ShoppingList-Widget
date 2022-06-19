package com.siddydevelops.shoppinglist.other

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.siddydevelops.shoppinglist.R
import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem
import com.siddydevelops.shoppinglist.ui.shopingList.ShoppingViewModel
import kotlinx.android.synthetic.main.shoping_item.view.*

class ShopingItemAdapter(
    var items: List<ShoppingItem>,
    private val viewModel: ShoppingViewModel
) : RecyclerView.Adapter<ShopingItemAdapter.ShopingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shoping_item,parent,false)
        return ShopingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopingViewHolder, position: Int) {
        val curShopingItem = items[position]
        holder.itemView.tvName.text = curShopingItem.name
        holder.itemView.tvAmount.text = "${curShopingItem.amount}"

        holder.itemView.ivDelete.setOnClickListener {
            viewModel.delete(curShopingItem)
        }
        holder.itemView.ivPlus.setOnClickListener {
            curShopingItem.amount++
            viewModel.upSort(curShopingItem)
        }
        holder.itemView.ivMinus.setOnClickListener {
            if(curShopingItem.amount > 0){
                curShopingItem.amount--
            }
            viewModel.upSort(curShopingItem)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ShopingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}