package com.tom.expense;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by hank on 2018/3/10.
 */

public class AddExpenseService extends IntentService {
    public static final String TAG = AddExpenseService.class.getSimpleName();
    public static final String ACTION_LAST_INSERT = "com.tom.expense.last_insert";

    public AddExpenseService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ContentValues values = intent.getParcelableExtra("EXPENSE_VALUES");
        getContentResolver().insert(ExpenseContract.CONTENT_URI,
                values);
        if (intent.getBooleanExtra("LAST", false)){
            Intent lastIntent = new Intent(ACTION_LAST_INSERT);
            sendBroadcast(lastIntent);
        }
    }

    public static void insert(Context context, ContentValues values, boolean last) {
        Intent intent = new Intent(context, AddExpenseService.class);
        intent.putExtra("EXPENSE_VALUES", values);
        intent.putExtra("LAST", last);
        context.startService(intent);
    }
}
