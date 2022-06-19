package com.siddydevelops.shoppinglist

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.lifecycle.LiveData
import com.siddydevelops.shoppinglist.data.db.ShoppingDatabase
import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem
import com.siddydevelops.shoppinglist.data.repositories.ShoppingRepository

class AppWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetItemFactory(applicationContext,intent)
    }

    inner class WidgetItemFactory(private val context: Context, intent: Intent) : RemoteViewsFactory {

        private val appWidgetId: Int = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID)

        private var shoppingRepository: ShoppingRepository
        private lateinit var allShoppingItems: LiveData<List<ShoppingItem>>
        private var shoppingItemList = ArrayList<ShoppingItem>()
        private var dao = ShoppingDatabase(context)

        init {
            shoppingRepository = ShoppingRepository(dao)
        }

        override fun onCreate() {
            //Connect to datasource
            allShoppingItems = shoppingRepository.getAllShoppingItems()
            allShoppingItems.observeForever { list->
                updateList(list)
            }
            //SystemClock.sleep(3000)
        }

        override fun onDataSetChanged() {
            allShoppingItems = shoppingRepository.getAllShoppingItems()
//            allShoppingItems.observeForever { list->
//                updateList(list)
//            }
            Log.d("DataChange","trigger")
            if(shoppingItemList.isNotEmpty()) {
                getViewAt(0)
            }
        }

        override fun onDestroy() {
            //Close datasource connection
        }

        override fun getCount(): Int {
            return shoppingItemList.size
        }

        override fun getViewAt(position: Int): RemoteViews {

            val fillIntentAdd = Intent()
            fillIntentAdd.putExtra("UpdateType","INC")
            fillIntentAdd.putExtra("CurName",shoppingItemList[position].name)
            fillIntentAdd.putExtra("CurAmount",shoppingItemList[position].amount)

            val fillIntentSub = Intent()
            fillIntentSub.putExtra("UpdateType","DEC")
            fillIntentSub.putExtra("CurName",shoppingItemList[position].name)
            fillIntentSub.putExtra("CurAmount",shoppingItemList[position].amount)

            val fillIntentDel = Intent()
            fillIntentDel.putExtra("UpdateType","DEL")
            fillIntentDel.putExtra("CurName",shoppingItemList[position].name)
            fillIntentDel.putExtra("CurAmount",shoppingItemList[position].amount)

            val remoteViews = RemoteViews(context.packageName, R.layout.shoping_item)
            remoteViews.setTextViewText(R.id.tvName, shoppingItemList[position].name)
            remoteViews.setTextViewText(R.id.tvAmount, shoppingItemList[position].amount.toString())
            remoteViews.setOnClickFillInIntent(R.id.ivPlus,fillIntentAdd)
            remoteViews.setOnClickFillInIntent(R.id.ivMinus,fillIntentSub)
            remoteViews.setOnClickFillInIntent(R.id.ivDelete,fillIntentDel)
            return remoteViews
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        private fun updateList(newList: List<ShoppingItem>) {
            shoppingItemList.clear()
            shoppingItemList.addAll(newList)
            onDataSetChanged()
        }

    }

}