package com.siddydevelops.shoppinglist.widgeting

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import androidx.lifecycle.LiveData
import com.siddydevelops.shoppinglist.R
import com.siddydevelops.shoppinglist.data.db.ShoppingDatabase
import com.siddydevelops.shoppinglist.data.db.entities.ShoppingItem
import com.siddydevelops.shoppinglist.data.repositories.ShoppingRepository
import com.siddydevelops.shoppinglist.ui.shopingList.ShoppingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppWidget : AppWidgetProvider() {

    val inc = "UPDATE_ITEM"

    private lateinit var shoppingRepository: ShoppingRepository
    private lateinit var allShoppingItems: LiveData<List<ShoppingItem>>
    private var shoppingItemList = ArrayList<ShoppingItem>()
    private lateinit var dao: ShoppingDatabase

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        dao = ShoppingDatabase(context!!)
        shoppingRepository = ShoppingRepository(dao)
        allShoppingItems = shoppingRepository.getAllShoppingItems()
        allShoppingItems.observeForever { list ->
            updateList(list)
        }

        val widgetManager = AppWidgetManager.getInstance(context.applicationContext)
        widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(ComponentName(context.applicationContext.packageName,
            AppWidget::class.java.name)),
            R.id.widget_listview
        )

        for (appWidgetId in appWidgetIds!!) {

            val addIntent = Intent(context, javaClass)
            addIntent.action = inc
            val addPendingIntent = PendingIntent.getBroadcast(context, 0, addIntent, 0)

            val serviceIntent = Intent(context, AppWidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

            val intent = Intent(context, ShoppingActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val views = RemoteViews(context.packageName, R.layout.app_widget_layout)
            views.setOnClickPendingIntent(R.id.openAppTV, pendingIntent)
            views.setRemoteAdapter(R.id.widget_listview, serviceIntent)
            views.setEmptyView(R.id.widget_listview, R.id.widget_empty_view)
            views.setPendingIntentTemplate(R.id.widget_listview, addPendingIntent)
            appWidgetManager!!.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        val widgetManager = AppWidgetManager.getInstance(context?.applicationContext)
        widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(ComponentName(context?.applicationContext!!.packageName,
            AppWidget::class.java.name)),
            R.id.widget_listview
        )
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        dao = ShoppingDatabase(context!!)
        shoppingRepository = ShoppingRepository(dao)
        when(intent!!.action) {
            inc -> {
                val curName = intent.getStringExtra("CurName")!!
                val intentType = intent.getStringExtra("UpdateType")!!
                when (intentType) {
                    "INC" -> {
                        val newAmount = intent.getIntExtra("CurAmount", 0) + 1
                        CoroutineScope(Dispatchers.Main).launch {
                            shoppingRepository.updateAmount(newAmount, curName)
                            val widgetManager =
                                AppWidgetManager.getInstance(context.applicationContext)
                            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(ComponentName(context.applicationContext.packageName,
                                AppWidget::class.java.name)),
                                R.id.widget_listview
                            )
                        }
                    }
                    "DEC" -> {
                        val newAmount = intent.getIntExtra("CurAmount", 0) - 1
                        CoroutineScope(Dispatchers.Main).launch {
                            shoppingRepository.updateAmount(newAmount, curName)
                            val widgetManager =
                                AppWidgetManager.getInstance(context.applicationContext)
                            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(ComponentName(context.applicationContext.packageName,
                                AppWidget::class.java.name)),
                                R.id.widget_listview
                            )
                        }
                    }
                    "DEL" -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            val curAmount = intent.getIntExtra("CurAmount", 0)
                            shoppingRepository.delete(ShoppingItem(curName,curAmount))
                            val widgetManager =
                                AppWidgetManager.getInstance(context.applicationContext)
                            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(ComponentName(context.applicationContext.packageName,
                                AppWidget::class.java.name)),
                                R.id.widget_listview
                            )
                        }
                    }
                }
            }
        }
        super.onReceive(context, intent)
    }

    private fun updateList(newList: List<ShoppingItem>) {
        shoppingItemList.clear()
        shoppingItemList.addAll(newList)
    }

}