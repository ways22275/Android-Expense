package com.tom.expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hank on 2018/2/24.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "expense.db";
    private static final int DB_VERSION = 1;
    private static DBHelper instance;
    private final Context context;

    public static DBHelper getInstance(Context context){
        if (instance == null){
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ExpenseContract.CREATE_SQL);
        // read expenses from resources
        readExpensesFromResources(db);

    }

    private void readExpensesFromResources(SQLiteDatabase db) {
        InputStream is = context.getResources().openRawResource(R.raw.expenses);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        try {
            String line = in.readLine();
            while(line != null){
                sb.append(line);
                line = in.readLine();
            }
            String json = sb.toString();
            JSONObject obj = new JSONObject(json);
            JSONArray expenses = obj.getJSONArray("expenses");
            for (int i = 0; i < expenses.length(); i++) {
                JSONObject exp = expenses.getJSONObject(i);
                String date = exp.getString("cdate");
                String info = exp.getString("info");
                int amount = exp.getInt("amount");
                ContentValues values = new ContentValues();
                values.put("cdate", date);
                values.put("info", info);
                values.put("amount", amount);
                if (i == expenses.length()-1){
                    AddExpenseService.insert(context, values, true);
                }else{
                    AddExpenseService.insert(context, values, false);
                }

//                long id =
//                    db.insert(ExpenseContract.TABLE_EXPENSE, null, values);

                // context.getContentResolver()
                //     .insert(ExpenseContract.CONTENT_URI, values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
