package com.tom.expense;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

/**
 * Created by hank on 2018/3/17.
 */

public class NotificationService extends IntentService {
    private static final String TAG = NotificationService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 99;
    private static final int REQUEST_DETAIL = 20;

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //TODO: Eric's job
        Log.d(TAG, "onHandleIntent: ");
        NotificationManager manager = getNotificationManager();
        Intent detailIntent = new Intent(this, DetailActivity.class);
        // get a random expense
        Cursor cursor = getContentResolver()
                .query(ExpenseContract.CONTENT_URI, null, null, null, null);
        Random r = new Random();
        int randomPosition = r.nextInt(cursor.getCount());
        cursor.moveToPosition(randomPosition);
        Expense expense = new Expense(cursor);
        detailIntent.putExtra("EXPENSE", expense);
        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivities(this,
                        REQUEST_DETAIL, new Intent[]{mainIntent, detailIntent},
                        PendingIntent.FLAG_UPDATE_CURRENT);
                /*PendingIntent.getActivity(this,
                        REQUEST_DETAIL,
                        detailIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);*/
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Titile")
                        .setContentText("This is text")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentIntent(pendingIntent)
                        .setChannelId("home")
                        .build();
        manager.notify(NOTIFICATION_ID, notification);
    }

    @NonNull
    private NotificationManager getNotificationManager() {
        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //8.0

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    "home",
                    "家人", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        return manager;
    }

    public static void startNotification(Context context) {
        Intent intent =
                new Intent(context, NotificationService.class);
        context.startService(intent);
    }
}
