package com.tom.expense;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by hank on 2018/3/24.
 */

public class UpdateWidgetService extends IntentService {

    private static final int REQUEST_WIDGET = 10;

    public UpdateWidgetService(){
        this("UpdateWidgetService");
    }
    public UpdateWidgetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Cursor cursor = getContentResolver()
                .query(ExpenseContract.CONTENT_URI, null, null, null, null);
        int random = new Random().nextInt(cursor.getCount());
        cursor.moveToPosition(random);
        Expense expense = new Expense(cursor);
        //
        int[] widgetIds = AppWidgetManager.getInstance(this)
                .getAppWidgetIds(new ComponentName(
                        getPackageName(),
                        ExpenseAppWidget.class.getName()));
        for (int widgetId : widgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(),
                    R.layout.expense_app_widget);
            views.setTextViewText(R.id.appwidget_text, expense.getInfo());
            Intent detailIntent = new Intent(this, DetailActivity.class);
            Intent mainIntent = new Intent(this, MainActivity.class);
            detailIntent.putExtra("EXPENSE", expense);
            PendingIntent pendingIntent =
                    PendingIntent.getActivities(this,
                            REQUEST_WIDGET, new Intent[]{mainIntent, detailIntent},
                            PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
            AppWidgetManager.getInstance(this)
                    .updateAppWidget(widgetId, views);
        }
    }
}
