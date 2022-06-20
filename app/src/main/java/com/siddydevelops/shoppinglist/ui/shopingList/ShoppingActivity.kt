package com.siddydevelops.shoppinglist.ui.shopingList

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.siddydevelops.shoppinglist.AppWidget
import com.siddydevelops.shoppinglist.R
import com.siddydevelops.shoppinglist.data.db.ShoppingDatabase
import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem
import com.siddydevelops.shoppinglist.data.repositories.ShoppingRepository
import com.siddydevelops.shoppinglist.other.ShopingItemAdapter
import kotlinx.android.synthetic.main.activity_shopping.*

class ShoppingActivity : AppCompatActivity() {
    private lateinit var widgetManager :AppWidgetManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        val database = ShoppingDatabase(this)
        val repository = ShoppingRepository(database)
        val factory = ShopingViewModelFactory(repository)

        val viewModel = ViewModelProviders.of(this,factory).get(ShoppingViewModel::class.java)

        val adapter = ShopingItemAdapter(listOf(),viewModel)

        rvShoppingItems.layoutManager = LinearLayoutManager(this)
        rvShoppingItems.adapter = adapter

        widgetManager = AppWidgetManager.getInstance(applicationContext)

        viewModel.getAllShoppingItems().observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(ComponentName(applicationContext.packageName,AppWidget::class.java.name)),
                R.id.widget_listview
            )
        })

        fab.setOnClickListener {
            AddShoppingItemDialog(this,
            object : AddDialogListener{
                override fun onAddButtonClick(item: ShoppingItem) {
                    viewModel.upSort(item)
                }
            }).show()
        }

    }
}