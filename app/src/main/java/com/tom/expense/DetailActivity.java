package com.tom.expense;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        expense = getIntent().getParcelableExtra("EXPENSE");
        ((TextView) findViewById(R.id.detail_date))
                .setText(expense.getDate());
        ((TextView) findViewById(R.id.detail_info))
                .setText(expense.getInfo());
        ((TextView) findViewById(R.id.detail_amount))
                .setText(expense.getAmount()+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete){
            getContentResolver().delete(ExpenseContract.CONTENT_URI,
                    ExpenseContract.COL_ID + " = ?" ,
                    new String[]{expense.getId()+""});
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
