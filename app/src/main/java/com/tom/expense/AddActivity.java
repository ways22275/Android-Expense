package com.tom.expense;

import android.content.ContentValues;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = AddActivity.class.getSimpleName();
    private EditText edDate;
    private EditText edInfo;
    private EditText edAmount;
    private TextInputLayout amountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        findViews();

    }

    private void findViews() {
        edDate = findViewById(R.id.date);
        edInfo = findViewById(R.id.info);
        edAmount = findViewById(R.id.amount);
        amountLayout = findViewById(R.id.amount_layout);
//        amountLayout.setError("amount wrong!!!");
    }

    public void add(View view){
        int amount = 0;
        String date = edDate.getText().toString();
        String info = edInfo.getText().toString();
        String amountText = edAmount.getText().toString();
        if (TextUtils.isEmpty(amountText)) {
            amountLayout.setError(getString(R.string.amount_is_empty_message));
        }else{
            amount = Integer.parseInt(amountText);
        }
//
        boolean reminder =
                PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_reminders", false);
        if (amount > 100 && reminder){

        }


        Log.d(TAG, "add: " + date + "/" + info + "/" + amount);
//        DBHelper helper = DBHelper.getInstance(this);
        ContentValues values = new ContentValues();
        values.put("cdate", date);
        values.put("info", info);
        values.put("amount", amount);

        /*Intent intent = new Intent(this, AddExpenseService.class);
        intent.putExtra("EXPENSE_VALUES", values);
        startService(intent);*/

        AddExpenseService.insert(this, values, true);

        /*Uri uri = getContentResolver().insert(
                ExpenseContract.CONTENT_URI,
                values);
//        long id = helper.getWritableDatabase()
//                .insert("exp", null, values);
        Log.d(TAG, "add: uri: " + uri);*/
    }
}
